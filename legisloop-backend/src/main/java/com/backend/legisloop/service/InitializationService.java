package com.backend.legisloop.service;


import com.backend.legisloop.entities.*;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.enums.VotePosition;
import com.backend.legisloop.model.LegiscanDataset;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.RollCall;
import com.backend.legisloop.repository.*;
import com.backend.legisloop.serial.BooleanSerializer;
import com.backend.legisloop.serial.DateSerializer;
import com.backend.legisloop.util.DatasetUtils;
import com.backend.legisloop.util.Utils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitializationService {

    private final LegislationRepository legislationRepository;
    private final RepresentativeRepository representativeRepository;
    private final LegislationDocumentRepository legislationDocumentRepository;
    private final RollCallRepository rollCallRepository;
    private final VoteRepository voteRepository;

    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;

    public List<LegiscanDataset> getDatasetListByYearAllStates(int year) throws UnirestException {

        StateEnum[] states = StateEnum.values();
        List<LegiscanDataset> datasetList = new ArrayList<>();

        for (StateEnum state : states) {
            datasetList.addAll(getDatasetListByYearForState(year, state));
        }

        return datasetList;
    }
    
    public List<LegiscanDataset> getDatasetListByYearForState(int year, StateEnum state) throws UnirestException {

        Gson gson = new Gson();
        List<LegiscanDataset> datasetList = new ArrayList<>();

            try {
                HttpResponse<JsonNode> response = Unirest.get(url + "/")
                        .queryString("key", API_KEY)
                        .queryString("op", "getDatasetList")
                        .queryString("state", state.name())
                        .queryString("year", year)
                        .asJson();

                if (response.getStatus() == 200) {
                    try {
                        JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                        Utils.checkLegiscanResponseStatus(jsonObject);

                        JsonArray stateDataset = jsonObject.getAsJsonArray("datasetlist");
                        Type listType = new TypeToken<List<LegiscanDataset>>() {}.getType();
                        datasetList.addAll(gson.fromJson(stateDataset, listType));
                    } catch (JsonSyntaxException | IllegalStateException e) {
                        log.error("Error parsing JSON response: {}", e.getMessage(), e);
                    }
                } else {
                    log.error("Failed to fetch bills for state: {}, year: {}. Server responded with status: {}",
                            state, year, response.getStatus());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Failed to fetch bills, server responded with status: " + response.getStatus());
                }
            } catch (UnirestException e) {
                log.error("Error making API request for state: {}, year: {} - {}", state, year, e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Failed to fetch bills, server responded with status: " + e.getMessage());
            }
            
        return datasetList;
    }

    public LegiscanDataset getDatasetByAccessKeyAndSession(int sessionId, String accessKey) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getDataset")
                .queryString("id", sessionId)
                .queryString("access_key", accessKey)
                .asJson();

        if (response.getStatus() == 200) {
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                Utils.checkLegiscanResponseStatus(jsonObject);

                JsonObject stateDataset = jsonObject.getAsJsonObject("dataset");
                return gson.fromJson(stateDataset, LegiscanDataset.class);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch bills, server responded with status: " + response.getStatus());
        }
    }
    
    public String initializeDbFromLegiscanAll() throws UnirestException, IOException {

        List<LegiscanDataset> datasetList = getDatasetListByYearAllStates(2025);
        return initializeDbFromLegiscanDatasets(datasetList);
    }
    
    public String initializeDbFromLegiscanByState(StateEnum state) throws UnirestException, IOException {

        List<LegiscanDataset> datasetList = getDatasetListByYearForState(2025, state);
        return initializeDbFromLegiscanDatasets(datasetList);
    }
    
    private String initializeDbFromLegiscanDatasets(List<LegiscanDataset> datasetList) throws UnirestException, IOException {
    	for (LegiscanDataset dataset : datasetList) {
    		
	        LegiscanDataset encodedZip = getDatasetByAccessKeyAndSession(dataset.getSession_id(), dataset.getAccess_key());
	
	        // Get the current date in YYYY-MM-DD format
	        String date = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
	
	        String outputFilePath = "../Datasets/" + StateEnum.fromStateID(dataset.getState_id()) + "_" + date + ".zip";
	        saveBase64ZipToFile(encodedZip.getZip(), outputFilePath);
	
	        Map<String, List<JsonObject>> dataMap = DatasetUtils.unzipJsonFiles(encodedZip.getZip());
	
	        saveDataFromZip(dataMap);
        }

        return "Initialized from API data";
    }
    
    public String initializeDbFromFilesystem(String filePath) throws IOException {
        File zipFile = new File(filePath);
        if (!zipFile.exists() || !zipFile.isFile()) {
            throw new IllegalArgumentException("ZIP file not found at: " + filePath);
        }

        Map<String, List<JsonObject>> dataMap = DatasetUtils.unzipJsonFilesFromFile(zipFile);

        saveDataFromZip(dataMap);

        return "Initialized from ZIP file in filesystem";
    }

    private void saveDataFromZip(Map<String, List<JsonObject>> dataMap) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(java.sql.Date.class, new DateSerializer()) // Register adapter
                .create();

        // Save Representatives
        List<JsonObject> representativeData = dataMap.getOrDefault("people", Collections.emptyList());
        List<RepresentativeEntity> representativesToAdd = new ArrayList<>();
        representativeData.forEach(representative -> {
            JsonObject representativeJson = representative.getAsJsonObject("person");
            representativesToAdd.add(gson.fromJson(representativeJson, RepresentativeEntity.class));
        });
        representativeRepository.saveAll(representativesToAdd);

        // Save Legislation
        List<JsonObject> legislationData = dataMap.getOrDefault("bill", Collections.emptyList());
        List<LegislationEntity> legislationToAdd = new ArrayList<>();
        legislationData.forEach(legislation -> {
            JsonObject legislationJson = legislation.getAsJsonObject("bill");
            LegislationEntity legislationEntity = Legislation.fillRecord(legislationJson).toEntity();
            
            // For each document, check if it exists in the repository
            List<LegislationDocumentEntity> existingDocs = new ArrayList<>();
            List<LegislationDocumentEntity> newDocs = new ArrayList<>();
            
            for (LegislationDocumentEntity doc : legislationEntity.getTexts()) {
                Optional<LegislationDocumentEntity> existingDoc = legislationDocumentRepository.findById(doc.getDoc_id());
                if (existingDoc.isPresent()) {
                    // If document exists, keep the existing one (preserves content)
                    existingDocs.add(existingDoc.get());
                } else {
                    // If document is new, add it
                    doc.setBill(LegislationEntity.builder().bill_id(legislationEntity.getBill_id()).build());
                    newDocs.add(doc);
                }
            }
            
            // Combine existing and new documents
            legislationEntity.setTexts(existingDocs);
            legislationEntity.getTexts().addAll(newDocs);
            
            legislationToAdd.add(legislationEntity);
        });
        legislationRepository.saveAll(legislationToAdd);

        // Save Roll Calls and Votes
        List<JsonObject> rollCallData = dataMap.getOrDefault("vote", Collections.emptyList());
        rollCallData.forEach(rollCall -> {
            JsonObject rollCallJson = rollCall.getAsJsonObject("roll_call");
            RollCallEntity rollCallToAdd = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .registerTypeAdapter(boolean.class, new BooleanSerializer())
                    .create()
                    .fromJson(rollCallJson, RollCall.class)
                    .toEntity();

            JsonArray votesArray = rollCallJson.getAsJsonArray("votes");
            List<VoteEntity> votesToAdd = new ArrayList<>();
            if (votesArray != null) {
                votesArray.forEach(rollCallVote -> {
                    VoteEntity vote = VoteEntity.builder()
                            .rollCall(rollCallToAdd)
                            .representative(RepresentativeEntity.builder()
                                    .people_id(rollCallVote.getAsJsonObject().get("people_id").getAsInt())
                                    .build())
                            .vote_position(VotePosition.fromVoteID(rollCallVote.getAsJsonObject().get("vote_id").getAsInt()))
                            .build();

                    votesToAdd.add(vote);
                });
            }

            rollCallToAdd.setVotes(votesToAdd);
            rollCallRepository.save(rollCallToAdd);
        });
    }

    private static void saveBase64ZipToFile(String base64String, String filePath) {
    	
    	File file = new File(filePath);
        // Create parent directories if they do not exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    	
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            // Decode the Base64 string into byte array
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);

            // Write to file
            fileOutputStream.write(decodedBytes);

            log.info("Zip file saved successfully at: " + filePath);
        } catch (IOException e) {
            log.error("Error saving zip file: " + e.getMessage());
        }
    }
}

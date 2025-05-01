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
	
            log.info("Saving data for ZIP: {}", outputFilePath);
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
        List<RepresentativeEntity> representativesToAdd = new ArrayList<RepresentativeEntity>();
        List<RepresentativeEntity> representativesToUpdate = new ArrayList<RepresentativeEntity>();
        log.info("\t{} Representatives in ZIP", representativeData.size());
        
        representativeData.forEach(representative -> {
            JsonObject representativeJson = representative.getAsJsonObject("person");
            RepresentativeEntity newRep = gson.fromJson(representativeJson, RepresentativeEntity.class);
            
            // Check if representative exists and if their hash has changed
            Optional<RepresentativeEntity> existingRep = representativeRepository.findById(newRep.getPeople_id());
            if (existingRep.isPresent()) {
                if (!existingRep.get().getPerson_hash().equals(newRep.getPerson_hash())) {
                    // Only update if the hash has changed
                    representativesToUpdate.add(newRep);
                }
            } else {
                // New representative
                representativesToAdd.add(newRep);
            }
        });
        
        log.info("\tAdding {} and updating {} Representative records", representativesToAdd.size(), representativesToUpdate.size());
        if (!representativesToAdd.isEmpty()) { // Save new representatives
            representativeRepository.saveAll(representativesToAdd);
        }
        if (!representativesToUpdate.isEmpty()) { // Update changed representatives
            representativeRepository.saveAll(representativesToUpdate);
        }

        // Save Legislation
        List<JsonObject> legislationData = dataMap.getOrDefault("bill", Collections.emptyList());
        List<LegislationEntity> legislationToAdd = new ArrayList<LegislationEntity>();
        List<LegislationEntity> legislationToUpdate = new ArrayList<LegislationEntity>();
        log.info("\t{} bills/legislation in ZIP", legislationData.size());
        
        legislationData.forEach(legislation -> {
            JsonObject legislationJson = legislation.getAsJsonObject("bill");
            LegislationEntity newLegislation = Legislation.fillRecord(legislationJson).toEntity();
            
            // Check if legislation exists and if its hash has changed
            Optional<LegislationEntity> existingLegislation = legislationRepository.findById(newLegislation.getBill_id());
            if (existingLegislation.isPresent()) {
                if (!existingLegislation.get().getChange_hash().equals(newLegislation.getChange_hash())) {
                    // Hash changed, update the legislation but preserve existing documents
                    List<LegislationDocumentEntity> existingDocs = new ArrayList<LegislationDocumentEntity>();
                    List<LegislationDocumentEntity> newDocs = new ArrayList<LegislationDocumentEntity>();
                    
                    for (LegislationDocumentEntity doc : newLegislation.getTexts()) {
                        Optional<LegislationDocumentEntity> existingDoc = legislationDocumentRepository.findById(doc.getDoc_id());
                        if (existingDoc.isPresent()) {
                            // If document exists, keep the existing one (preserves content)
                            existingDocs.add(existingDoc.get());
                        } else {
                            // If document is new, add it
                            doc.setBill(LegislationEntity.builder().bill_id(newLegislation.getBill_id()).build());
                            newDocs.add(doc);
                        }
                    }
                    
                    // Combine existing and new documents
                    newLegislation.setTexts(existingDocs);
                    newLegislation.getTexts().addAll(newDocs);
                    legislationToUpdate.add(newLegislation);
                }
            } else {
                // New legislation
                legislationToAdd.add(newLegislation);
            }
        });
        
        log.info("\tAdding {} and updating {} Legislation records", legislationToAdd.size(), legislationToUpdate.size());
        if (!legislationToAdd.isEmpty()) { // Save new legislation
            legislationRepository.saveAll(legislationToAdd);
        }
        if (!legislationToUpdate.isEmpty()) { // Update changed legislation
            legislationRepository.saveAll(legislationToUpdate);
        }

        // Save Roll Calls and Votes
        List<JsonObject> rollCallData = dataMap.getOrDefault("vote", Collections.emptyList());
        List<RollCallEntity> rollCallsToAdd = new ArrayList<RollCallEntity>();
        log.info("\t{} RollCalls in ZIP", rollCallData.size());

        rollCallData.forEach(rollCall -> {
            JsonObject rollCallJson = rollCall.getAsJsonObject("roll_call");
            RollCallEntity rollCallToAdd = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .registerTypeAdapter(boolean.class, new BooleanSerializer())
                    .create()
                    .fromJson(rollCallJson, RollCall.class)
                    .toEntity();

            Optional<RollCallEntity> existingRollCall = rollCallRepository.findById(rollCallToAdd.getRoll_call_id());
            if (existingRollCall.isPresent()) return; // Roll calls and their votes do not change

            JsonArray votesArray = rollCallJson.getAsJsonArray("votes");
            List<VoteEntity> votesToAdd = new ArrayList<VoteEntity>();
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
            rollCallsToAdd.add(rollCallToAdd);
        });
        
        log.info("\tAdding {} RollCall records", rollCallsToAdd.size());
        if (!rollCallsToAdd.isEmpty()) { // Save new legislation
            rollCallRepository.saveAll(rollCallsToAdd);
        }
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

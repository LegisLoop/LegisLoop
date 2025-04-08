package com.backend.legisloop.service;


import com.backend.legisloop.entities.*;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.enums.VotePosition;
import com.backend.legisloop.model.LegiscanDataset;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.Date;

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

    public List<LegiscanDataset> getDatasetListByYear(int year) {

        StateEnum[] states = StateEnum.values();
        List<LegiscanDataset> datasetList = new ArrayList<>();
        Gson gson = new Gson();

        for (StateEnum state : states) {
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
            }
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
    
    public String initializeDbFromLegisacn() throws UnirestException, IOException {

        List<LegiscanDataset> datasetList = getDatasetListByYear(2025);
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
            LegislationEntity legislationEntity = gson.fromJson(legislationJson, LegislationEntity.class);
            
            legislationEntity.setState(StateEnum.fromStateID(legislationJson.get("state_id").getAsInt()));
            
            for (LegislationDocumentEntity doc : legislationEntity.getTexts()) {
            	doc.setBill(LegislationEntity.builder().bill_id(legislationEntity.getBill_id()).build());
            }

            JsonArray progress = legislationJson.getAsJsonArray("progress");
            // Find the date where event = 1 (introduced)
            LocalDate event1Date = null;
            if(progress != null) {
                for (JsonElement element : progress) {
                    JsonObject progressObject = element.getAsJsonObject();
                    int event = progressObject.get("event").getAsInt();

                    if (event == 1) {
                        String dateIntroduced = progressObject.get("date").getAsString();
                        event1Date = LocalDate.parse(dateIntroduced, DateTimeFormatter.ISO_DATE);
                        break;
                    }
                }
            }
            legislationEntity.setDateIntroduced((event1Date != null) ? Date.valueOf(event1Date) : null);
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


    public void insertDummyData() {
        // Insert Representatives
        RepresentativeEntity rep1 = RepresentativeEntity.builder()
                .people_id(1)
                .name("John Doe")
                .party("Democrat")
                .state_id(1)
                .role("Senator")
                .role_id(1)
                .build();

        RepresentativeEntity rep2 = RepresentativeEntity.builder()
                .people_id(2)
                .name("Jane Smith")
                .party("Republican")
                .state_id(2)
                .role("Representative")
                .role_id(1)
                .build();

        representativeRepository.saveAll(Arrays.asList(rep1, rep2));

        // Insert and Save Roll Calls First
        RollCallEntity rollCall1 = RollCallEntity.builder()
                .roll_call_id(98)
                .absent(1)
                .nv(2)
                .yea(3)
                .nay(4)
                .passed(false)
                .total(10)
                .desc("Baller vote")
                .build();

        RollCallEntity rollCall2 = RollCallEntity.builder()
                .roll_call_id(99)
                .absent(5)
                .nv(6)
                .yea(7)
                .nay(8)
                .passed(false)
                .total(20)
                .desc("Mega baller vote")
                .build();

        rollCallRepository.saveAll(Arrays.asList(rollCall1, rollCall2));

        // Fetch roll calls again to ensure IDs are set
        rollCall1 = rollCallRepository.findById(rollCall1.getRoll_call_id()).orElseThrow();
        rollCall2 = rollCallRepository.findById(rollCall2.getRoll_call_id()).orElseThrow();

        // Insert Legislation
        LegislationEntity legislation = LegislationEntity.builder()
                .bill_id(100)
                .title("Clean Energy Act")
                .description("A bill to promote renewable energy")
                .summary("This bill provides incentives for clean energy projects.")
                .change_hash("xyz123")
                .url("https://example.com/legislation/101")
                .state_link("https://state.example.com/legislation/101")
                .sponsors(List.of(rep1, rep2))
                // .endorsements(List.of(rep1))
                .rollCalls(List.of(rollCall1, rollCall2))
                .build();

        legislationRepository.save(legislation);

        // Insert Legislation Documents
        LegislationDocumentEntity document = LegislationDocumentEntity.builder()
                .doc_id(1)
                .bill(legislation)
                .text_hash("doc_hash_123")
                .url(URI.create("https://example.com/document/1"))
                .state_link(URI.create("https://external.example.com/document/1"))
                .mime("application/pdf")
                .mimeId(1)
                .docContent("Sample document content")
                .type("Bill Text")
                .type_id(101)
                .build();

        legislationDocumentRepository.save(document);

        // Insert Votes After Roll Calls Exist
        VoteEntity vote1 = VoteEntity.builder()
                .rollCall(rollCall1)
                .representative(rep1)
                .vote_position(VotePosition.YEA)
                .build();

        VoteEntity vote2 = VoteEntity.builder()
                .rollCall(rollCall1)
                .representative(rep2)
                .vote_position(VotePosition.NAY)
                .build();

        VoteEntity vote3 = VoteEntity.builder()
                .rollCall(rollCall2)
                .representative(rep1)
                .vote_position(VotePosition.NV)
                .build();

        VoteEntity vote4 = VoteEntity.builder()
                .rollCall(rollCall2)
                .representative(rep2)
                .vote_position(VotePosition.ABSENT)
                .build();

        // Associate Votes with Roll Calls
        rollCall1.setVotes(Arrays.asList(vote1, vote2));
        rollCall2.setVotes(Arrays.asList(vote3, vote4));

        // Save Votes
        voteRepository.saveAll(Arrays.asList(vote1, vote2, vote3, vote4));

        // Save Roll Calls Again to Update with Votes
        rollCallRepository.saveAll(Arrays.asList(rollCall1, rollCall2));
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

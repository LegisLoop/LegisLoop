package com.backend.legisloop.service;


import com.backend.legisloop.entities.*;
import com.backend.legisloop.enums.VotePosition;
import com.backend.legisloop.model.LegiscanDataset;
import com.backend.legisloop.repository.*;
import com.backend.legisloop.util.Utils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

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

    public List<LegiscanDataset> getDatasetListByState(String state) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getDatasetList")
                .queryString("state", state)
                .asJson();

        if (response.getStatus() == 200) {
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                Utils.checkLegiscanResponseStatus(jsonObject);

                JsonArray datasetList = jsonObject.get("datasetlist").getAsJsonArray();
                Type listType = new TypeToken<List<LegiscanDataset>>() {}.getType();
                return gson.fromJson(datasetList, listType);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch bills, server responded with status: " + response.getStatus());
        }
    }

    @PostConstruct
    public void insertDummyData() {
        // Insert Representatives
        RepresentativeEntity rep1 = RepresentativeEntity.builder()
                .peopleId(1)
                .name("John Doe")
                .party("Democrat")
                .stateId(1)
                .role("Senator")
                .roleId(1)
                .build();

        RepresentativeEntity rep2 = RepresentativeEntity.builder()
                .peopleId(2)
                .name("Jane Smith")
                .party("Republican")
                .stateId(2)
                .role("Representative")
                .roleId(1)
                .build();

        representativeRepository.saveAll(Arrays.asList(rep1, rep2));

        // Insert Roll Calls
        RollCallEntity rollCall1 = RollCallEntity.builder()
                .bill_id(101)
                .absent(1)
                .nv(2)
                .yea(3)
                .nay(4)
                .passed(false)
                .total(10)
                .desc("Baller vote")
                .roll_call_id(999)
                .build();

        RollCallEntity rollCall2 = RollCallEntity.builder()
                .bill_id(101)
                .absent(5)
                .nv(6)
                .yea(7)
                .nay(8)
                .passed(false)
                .total(20)
                .desc("Mega baller vote")
                .roll_call_id(1000)
                .build();

        rollCallRepository.saveAll(Arrays.asList(rollCall1, rollCall2));

        // Insert Legislation
        LegislationEntity legislation = LegislationEntity.builder()
                .billId(101)
                .title("Clean Energy Act")
                .description("A bill to promote renewable energy")
                .summary("This bill provides incentives for clean energy projects.")
                .change_hash("xyz123")
                .url("https://example.com/legislation/101")
                .stateLink("https://state.example.com/legislation/101")
                .sponsors(List.of(rep1, rep2))
                .endorsements(List.of(rep1))
                .rollCalls(List.of(rollCall1, rollCall2))
                .build();

        legislationRepository.save(legislation);

        // Insert Legislation Documents
        LegislationDocumentEntity document = LegislationDocumentEntity.builder()
                .docId(1)
                .bill(legislation)
                .textHash("doc_hash_123")
                .legiscanLink(URI.create("https://example.com/document/1"))
                .externalLink(URI.create("https://external.example.com/document/1"))
                .mime("application/pdf")
                .mimeId(1)
                .docContent("Sample document content")
                .type("Bill Text")
                .typeId(101)
                .build();

        legislationDocumentRepository.save(document);

        // Insert votes
        VoteEntity vote1 = VoteEntity.builder()
                .bill(legislation)
                .rollCall(rollCall1)
                .representative(rep1)
                .vote_position(VotePosition.YEA)
                .build();

        VoteEntity vote2 = VoteEntity.builder()
                .bill(legislation)
                .rollCall(rollCall1)
                .representative(rep2)
                .vote_position(VotePosition.NAY)
                .build();

        VoteEntity vote3 = VoteEntity.builder()
                .bill(legislation)
                .rollCall(rollCall2)
                .representative(rep1)
                .vote_position(VotePosition.NV)
                .build();

        VoteEntity vote4 = VoteEntity.builder()
                .bill(legislation)
                .rollCall(rollCall2)
                .representative(rep2)
                .vote_position(VotePosition.ABSENT)
                .build();

        voteRepository.saveAll(Arrays.asList(vote1, vote2, vote3, vote4));
    }
}

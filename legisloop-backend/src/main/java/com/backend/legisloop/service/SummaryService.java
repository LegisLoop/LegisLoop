package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationDocumentEntity;
import com.backend.legisloop.entities.SummaryEntity;
import com.backend.legisloop.enums.ReadingLevelEnum;
import com.backend.legisloop.model.Summary;
import com.backend.legisloop.repository.LegislationDocumentRepository;
import com.backend.legisloop.repository.SummaryRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryService {

    @Value("${readeasy.api.key}")
    private String API_KEY;
    @Value("${readeasy.base.url}")
    private String url;

    private final SummaryRepository summaryRepository;
    private final LegislationDocumentRepository legislationDocumentRepository;

    public String getSummaryOfContent(String query) throws UnirestException {

        log.info("Fetching summary about {}...", query.substring(0, Math.min(query.length(), 20)));
        
    	JSONObject jsonBody = new JSONObject();
        jsonBody.put("text", query);

        HttpResponse<String> response = Unirest.post(url + "/text/showHighlights")
            .header("X-API-KEY", API_KEY)
            .header("Content-Language", "en")
            .header("Content-Type", "application/json")
            .body(jsonBody.toString())
            .asString();

        if (response.getStatus() == 200) {
            try{
                return response.getBody();
            } catch (Exception e){
                log.error(e.getMessage());
                throw e;
            }
        }
        else {
        	log.error("Failed to fetch summary, server responded with status: " + response.getStatus() + " and content:\n\t" + response.getBody());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch summary, server responded with status: " + response.getStatus() + " and content:\n\t" + response.getBody());
        }
    }

    @Transactional
    public String getSummaryOfContentByReadingLevel(int docId, String query, ReadingLevelEnum readingLevelEnum) throws UnirestException {
        int age = readingLevelEnum.getAge();
        log.info("Fetching summary for {} year olds about {}...", age, query.substring(0, Math.min(query.length(), 20)));
        
    	JSONObject jsonBody = new JSONObject();
        jsonBody.put("text", query);
        jsonBody.put("age", age);

        HttpResponse<String> response = Unirest.post(url + "/text/makeReadable")
            .header("X-API-KEY", API_KEY)
            .header("Content-Language", "en")
            .header("Content-Type", "application/json")
            .body(jsonBody.toString())
            .asString();

        if (response.getStatus() == 200) {
            try{
                String summaryText = response.getBody();
                LegislationDocumentEntity doc = legislationDocumentRepository.findById(docId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No LegislationDocument with id=" + docId
                        ));

                SummaryEntity summaryToAdd = SummaryEntity.builder()
                        .legislationDocument(doc)
                        .summaryContent(summaryText)
                        .readingLevel(readingLevelEnum)
                        .build();
                summaryRepository.save(summaryToAdd);

                return summaryText;
            } catch (Exception e){
                log.error(e.getMessage());
                throw e;
            }
        }
        else {
        	log.error("Failed to fetch summary, server responded with status: " + response.getStatus() + " and content:\n\t" + response.getBody());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch summary, server responded with status: " + response.getStatus() + " and content:\n\t" + response.getBody());
        }
    }

    public Summary getSummaryByDocIdAndReadingLevel(int docId, ReadingLevelEnum readingLevel) {
        return summaryRepository.findSummaryByDocIdAndReadingLevel(docId, readingLevel).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No summary found for docId: " + docId)
        ).toModel();
    }
}

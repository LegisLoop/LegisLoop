package com.backend.legisloop.service;

import com.backend.legisloop.repository.LegislationDocumentRepository;
import com.backend.legisloop.repository.LegislationRepository;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.backend.legisloop.entities.LegislationDocumentEntity;
import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.model.LegislationDocument;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
@Slf4j
@RequiredArgsConstructor
public class LegislationDocumentService {

	private final LegislationService legislationService;
    private final LegislationRepository legislationRepository;
    private final LegislationDocumentRepository legislationDocumentRepository;
	
    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;


    public LegislationDocument getLegislationDocById(int legislationDocumentId) throws UnirestException {
    	Optional<LegislationDocumentEntity> doc = legislationDocumentRepository.findById(legislationDocumentId);
    	if (doc.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Legislation document does not exist!");
    	
    	return getDocContent(doc.get());
    }
    
    public LegislationDocument getLatestLegislationDocForLegislation(int billId) throws UnirestException {
    	if (legislationRepository.findById(billId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill does not exist!");
    	LegislationDocumentEntity doc = legislationDocumentRepository.findMostRecentByBill(LegislationEntity.builder().bill_id(billId).build());
    	
    	return getDocContent(doc);
    }
    
    private LegislationDocument getDocContent(LegislationDocumentEntity incomingEntity) throws ResponseStatusException, UnirestException {
    	
    	if (incomingEntity == null) {
	    	throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No doc for this bill!");
    	}
    	
    	LegislationDocument doc = incomingEntity.toModel();
                
        if (doc.getDocContent() == null) {
    		doc = legislationService.getDocContent(doc);
    		legislationDocumentRepository.saveAndFlush(doc.toEntity());
    	}
    	
    	return doc;
    }
    
    


}

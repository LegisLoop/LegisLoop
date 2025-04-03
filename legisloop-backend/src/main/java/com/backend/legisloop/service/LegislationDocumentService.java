package com.backend.legisloop.service;

import com.backend.legisloop.repository.LegislationDocumentRepository;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.backend.legisloop.model.LegislationDocument;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Service
@Slf4j
@RequiredArgsConstructor
public class LegislationDocumentService {

	private final LegislationService legislationService;
    private final LegislationDocumentRepository legislationDocumentRepository;
	
    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;


    public LegislationDocument getLegislationById(int legislationDocumentId) throws UnirestException {
    	LegislationDocument doc = legislationDocumentRepository.getReferenceById(legislationDocumentId).toModel();
    	
    	if (doc.getDocContent() == null) {
    		doc = legislationService.getDocContent(doc);
    		legislationDocumentRepository.saveAndFlush(doc.toEntity());
    	}
    	
    	return doc;
    }


}

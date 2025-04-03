package com.backend.legisloop.controller;

import com.backend.legisloop.model.LegislationDocument;
import com.backend.legisloop.service.LegislationDocumentService;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/legislation-doc")
@RequiredArgsConstructor
public class LegislationDocumentController {

    private final LegislationDocumentService legislationDocService;

    // get legislation document by id
    @GetMapping("/{docID}")
    public ResponseEntity<LegislationDocument> getDoc(@PathVariable int docID) throws UnirestException  {
        return new ResponseEntity<>(legislationDocService.getLegislationById(docID), HttpStatus.OK);
    }
}
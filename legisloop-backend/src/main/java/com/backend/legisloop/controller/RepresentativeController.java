package com.backend.legisloop.controller;

import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.service.RepresentativeService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RepresentativeController {

    private final RepresentativeService representativeService;

    @GetMapping("/getSessionPeople")
    public ResponseEntity<List<Representative>> getSessionPeople(int sessionId) throws UnirestException {
        return new ResponseEntity<>(representativeService.getSessionPeople(sessionId), HttpStatus.OK);
    }

    @GetMapping("/getPersonBYId")
    public ResponseEntity<Representative> getPersonById(int personId) throws UnirestException {
        return new ResponseEntity<>(representativeService.getRepresentativeById(personId), HttpStatus.OK);
    }

    @GetMapping("/getSponsoredBills")
    public ResponseEntity<List<Legislation>> getSponsoredBills(int personId) throws UnirestException {
        return new ResponseEntity<>(representativeService.getRepresentativeBills(personId), HttpStatus.OK);
    }
}

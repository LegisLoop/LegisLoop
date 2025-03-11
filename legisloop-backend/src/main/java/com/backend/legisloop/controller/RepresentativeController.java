package com.backend.legisloop.controller;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.backend.legisloop.service.RepresentativeService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/representative")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RepresentativeController {

    private final RepresentativeService representativeService;

    private final RepresentativeRepository representativeRepository;

    // get all the people of a session. this still uses legiscan not the database
    @GetMapping("/getSessionPeople")
    public ResponseEntity<List<Representative>> getSessionPeople(int sessionId) throws UnirestException {

        return new ResponseEntity<>(representativeService.getSessionPeople(sessionId), HttpStatus.OK);
    }

    // get by id
    @GetMapping("/{personId}")
    public ResponseEntity<Representative> getPersonById(@PathVariable int personId) {

        return new ResponseEntity<>(representativeRepository.getReferenceById(personId).toModel(), HttpStatus.OK);
    }

    // get sponsors of a bill
    @GetMapping("/sponsorsOf/{billId}")
    public ResponseEntity<List<Representative>> getSponsorsByBill(@PathVariable int billId) {
        return new ResponseEntity<>(representativeService.getSponsorsByBillId(billId), HttpStatus.OK);
    }

    // search representatives by name (first and last included)
    @GetMapping("/search/name")
    public ResponseEntity<List<Representative>> searchRepresentatives(@RequestParam String keyword) {
        return new ResponseEntity<>(representativeService.searchRepresentatives(keyword), HttpStatus.OK);
    }
}

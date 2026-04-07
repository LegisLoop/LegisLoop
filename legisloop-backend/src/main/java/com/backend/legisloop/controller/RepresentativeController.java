package com.backend.legisloop.controller;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.backend.legisloop.service.RepresentativeService;
import com.backend.legisloop.service.SearchService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/representative")
@RequiredArgsConstructor
public class RepresentativeController {

    private final RepresentativeService representativeService;
    private final SearchService searchService;

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
    @GetMapping("/search")
    public ResponseEntity<?> searchRepresentatives(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(searchService.searchRepresentatives(name, page, size));
    }

    // get representatives by state id
    @GetMapping("/stateId/{stateId}")
    public ResponseEntity<List<Representative>> getRepresentativeByStateId(@PathVariable int stateId) {
        return new ResponseEntity<>(representativeRepository.findByStateId(stateId).stream().map(RepresentativeEntity::toModel).toList(), HttpStatus.OK);
    }

    // take in a list of people_ids in the body and output a mapping of them to the respective names
    @PostMapping("/names")
    public Map<Integer, String> getNamesByIds(@RequestBody List<Integer> peopleIds) {
        List<RepresentativeEntity> reps = representativeRepository.findAllById(peopleIds);

        return reps.stream()
                .collect(Collectors.toMap(
                        RepresentativeEntity::getPeople_id,  // key = the ID
                        RepresentativeEntity::getName        // value = the name
                ));
    }
}

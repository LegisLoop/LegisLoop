package com.backend.legisloop.controller;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.enums.PolicyAreasEnum;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.service.LegislationService;
import com.backend.legisloop.service.SearchService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Policy;
import java.util.List;

@RestController
@RequestMapping("/api/v1/legislation")
@RequiredArgsConstructor
public class LegislationController {

    private final LegislationService billService;
    private final LegislationRepository legislationRepository;
    private final SearchService searchService;

    // get legislation by id
    @GetMapping("/{legislationId}")
    public ResponseEntity<Legislation> getBill(@PathVariable int legislationId)  {

        return new ResponseEntity<>(billService.getLegislationById(legislationId), HttpStatus.OK);
    }
    
    @GetMapping("/getMasterListChange")
    public ResponseEntity<List<Legislation>> getMasterListChangeByState(@RequestParam String state) throws UnirestException {
        return new ResponseEntity<>(billService.getMasterListChange(state), HttpStatus.OK);
    }

    // get all legislation by state abbreviation (NJ, CA, etc.) (paginated)
    @GetMapping("/state/{state}/paginated")
    public ResponseEntity<Page<Legislation>> getLegislationByState(
            @PathVariable StateEnum state,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return new ResponseEntity<>(billService.getLegislationByState(state, page, size), HttpStatus.OK);
    }

    // get all legislation by stateId (paginated)
    @GetMapping("/stateId/{stateId}/paginated")
    public ResponseEntity<Page<Legislation>> getLegislationByStateId(
            @PathVariable int stateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(billService.getLegislationByStateId(stateId, page, size), HttpStatus.OK);
    }
    
    // Get a person's sponsored bills (paginated)
    @GetMapping("/sponsoredBills/{personId}/paginated")
    public ResponseEntity<Page<Legislation>> getSponsoredBills(
            @PathVariable int personId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return new ResponseEntity<>(billService.getLegislationByRepresentativeIdPaginated(personId, page, size), HttpStatus.OK);
    }

    // get legislation by session id
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Legislation>> getLegislationBySessionId(@PathVariable int sessionId) {
        return new ResponseEntity<>(legislationRepository.findBySessionId(sessionId).stream().map(LegislationEntity::toModel).toList(), HttpStatus.OK);
    }
    
    // get legislation by session id (paginated)
    @GetMapping("/session/{sessionId}/paginated")
    public ResponseEntity<Page<Legislation>> getLegislationBySessionIdPaginated(
            @PathVariable int sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Legislation> legislationPage = billService.getLegislationBySessionIdPaginated(sessionId, page, size);
        return ResponseEntity.ok(legislationPage);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchLegislation(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(searchService.searchLegislation(query, page, size));
    }

    @GetMapping("/search/keyword")
    public ResponseEntity<?> searchLegislationKeyword(
            @RequestParam(value = "keyword")PolicyAreasEnum keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(searchService.searchLegislationKeywords(keyword, page, size));
    }
}
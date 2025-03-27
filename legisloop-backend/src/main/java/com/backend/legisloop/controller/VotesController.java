package com.backend.legisloop.controller;

import com.backend.legisloop.model.Vote;
import com.backend.legisloop.repository.VoteRepository;
import com.backend.legisloop.service.VoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vote")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class VotesController {
	
    private final VoteService voteService;
    private final VoteRepository voteRepository;

    // get vote by id
    @GetMapping("/{voteId}")
    public ResponseEntity<Vote> testVoteDb(@PathVariable Long voteId) {
        return new ResponseEntity<>(voteRepository.getReferenceById(voteId).toModel(), HttpStatus.OK);
    }
    
    // Get a person's sponsored bills (paginated)
    @GetMapping("/votedBills/{personId}/paginated")
    public ResponseEntity<Page<Vote>> getSponsoredBills(
            @PathVariable int personId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return new ResponseEntity<>(voteService.getVotesByRepresentativeIdPaginated(personId, page, size), HttpStatus.OK);
    }
}

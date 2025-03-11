package com.backend.legisloop.controller;

import com.backend.legisloop.model.Vote;
import com.backend.legisloop.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vote")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class VotesController {
    private final VoteRepository voteRepository;

    // get vote by id
    @GetMapping("/{voteId}")
    public ResponseEntity<Vote> testVoteDb(@PathVariable Long voteId) {
        return new ResponseEntity<>(voteRepository.getReferenceById(voteId).toModel(), HttpStatus.OK);
    }
}

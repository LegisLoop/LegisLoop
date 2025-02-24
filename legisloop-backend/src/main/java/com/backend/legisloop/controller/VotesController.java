package com.backend.legisloop.controller;

import com.backend.legisloop.model.Vote;
import com.backend.legisloop.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class VotesController {
    private final VoteRepository voteRepository;

    @GetMapping("/testVoteDb")
    public ResponseEntity<Vote> testVoteDb(@RequestParam Long vote_id) {
        return new ResponseEntity<>(voteRepository.getReferenceById(vote_id).toModel(), HttpStatus.OK);
    }
}

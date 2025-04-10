package com.backend.legisloop.controller;

import com.backend.legisloop.service.SummaryService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping("/summary")
    public ResponseEntity<String> getSummary(@RequestBody String query) throws UnirestException {

        return new ResponseEntity<>(summaryService.getSummaryOfContent(query), HttpStatus.OK);
    }
    
    @PostMapping("/summary/{age}")
    public ResponseEntity<String> getSummaryByAge(@PathVariable int age, @RequestBody String query) throws UnirestException {

        return new ResponseEntity<>(summaryService.getSummaryOfContentByAge(query, age), HttpStatus.OK);
    }
}

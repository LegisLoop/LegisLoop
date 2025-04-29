package com.backend.legisloop.controller;

import com.backend.legisloop.enums.ReadingLevelEnum;
import com.backend.legisloop.model.Summary;
import com.backend.legisloop.service.SummaryService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping
    public ResponseEntity<String> getSummary(@RequestBody String query) throws UnirestException {

        return new ResponseEntity<>(summaryService.getSummaryOfContent(query), HttpStatus.OK);
    }
    
    @PostMapping("/readingLevel/{docId}")
    public ResponseEntity<String> getSummaryByAge(
            @PathVariable int docId,
            @RequestParam ReadingLevelEnum readingLevel,
            @RequestParam String mimeType,
            @RequestBody String encodedString
    ) throws UnirestException, IOException {

        return new ResponseEntity<>(summaryService.getSummaryOfContentByReadingLevel(docId, encodedString, readingLevel, mimeType), HttpStatus.OK);
    }

    @GetMapping("/readingLevel/{docId}")
    public ResponseEntity<Summary> getSummaryByDocIdAndReadingLevel(
            @PathVariable int docId,
            @RequestParam ReadingLevelEnum readingLevel
    ) {
        return ResponseEntity.ok(summaryService.getSummaryByDocIdAndReadingLevel(docId, readingLevel));
    }
}

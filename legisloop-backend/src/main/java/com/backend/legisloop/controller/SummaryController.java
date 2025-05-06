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

    @GetMapping("/readingLevel/{docId}")
    public ResponseEntity<Summary> getSummaryByDocIdAndReadingLevel(
            @PathVariable int docId,
            @RequestParam ReadingLevelEnum readingLevel
    ) throws UnirestException, IOException {
        return ResponseEntity.ok(summaryService.getSummaryByDocIdAndReadingLevel(docId, readingLevel));
    }
}

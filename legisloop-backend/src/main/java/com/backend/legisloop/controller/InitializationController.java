package com.backend.legisloop.controller;

import com.backend.legisloop.service.InitializationService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InitializationController {
    private final InitializationService initializationService;

    @PostMapping("/initializeDb/legiscan")
    public ResponseEntity<String> initializeDb() throws UnirestException, IOException {
        return new ResponseEntity<>(initializationService.initializeDbFromLegisacn(), HttpStatus.OK);
    }

    @PostMapping(value = "/initializeDb/files")
    public ResponseEntity<String> initializeDbZip(@RequestParam String filePath) throws IOException {
        return new ResponseEntity<>(initializationService.initializeDbFromFilesystem(filePath), HttpStatus.OK);
    }
}

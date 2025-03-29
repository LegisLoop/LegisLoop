package com.backend.legisloop.controller;

import com.backend.legisloop.service.InitializationService;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InitializationController {
    private final InitializationService initializationService;
    
    @Value("${legisloop.api.key}")
    private String apiPassword;

    @Operation(summary = "Initialize DB from LegiScan.",
    		description = "Takes multiple hours.",
            security = {@SecurityRequirement(name = "apiKey")})
    @PostMapping("/initializeDb/legiscan")
    public ResponseEntity<String> initializeDb() throws UnirestException, IOException {
        return new ResponseEntity<>(initializationService.initializeDbFromLegisacn(), HttpStatus.OK);
    }

    @Operation(summary = "Initialize DB from LegiScan DB ZIP files.",
    		description = "Takes multiple hours.",
            security = {@SecurityRequirement(name = "apiKey")})
    @PostMapping(value = "/initializeDb/files")
    public ResponseEntity<String> initializeDbZip(@RequestParam String filePath) throws IOException {
        return new ResponseEntity<>(initializationService.initializeDbFromFilesystem(filePath), HttpStatus.OK);
    }
}

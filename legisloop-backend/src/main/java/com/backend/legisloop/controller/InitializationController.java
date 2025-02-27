package com.backend.legisloop.controller;

import com.backend.legisloop.service.InitializationService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InitializationController {
    private final InitializationService initializationService;

    @GetMapping("/initializeDb")
    public boolean initializeDb() throws UnirestException, IOException {
        return initializationService.initializeDb();
    }
}

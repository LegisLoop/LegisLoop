package com.backend.legisloop.controller;

import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.service.LocationService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/state")
    public ResponseEntity<StateEnum> getState(@RequestParam double lat, @RequestParam double lon) throws UnirestException {
        return ResponseEntity.ok(locationService.getUserLocation(lat, lon));
    }
}

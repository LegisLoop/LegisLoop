package com.backend.legisloop.controller;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.service.BillService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BillsController {
    private final BillService billService;

    @GetMapping("/getMasterList")
    public ResponseEntity<List<Legislation>> getMasterListByState(@RequestParam String state) throws UnirestException {
        return new ResponseEntity<>(billService.getMasterList(state), HttpStatus.OK);
    }

    @GetMapping("/getBill")
    public ResponseEntity<Legislation> getBill(@RequestParam int bill_id, @RequestParam String state) throws UnirestException, URISyntaxException {
        List<Legislation> masterList = billService.getMasterList(state);
        Legislation legislationToFind = masterList.stream()
                .filter(legislation -> legislation.getBill_id() == bill_id)
                .findFirst()
                .orElse(null);
        return new ResponseEntity<>(billService.getBill(legislationToFind), HttpStatus.OK);
    }
    //TODO delete eventually
    @GetMapping("/testDb")
    public ResponseEntity<List<Legislation>> getAllLegislation() {
        return new ResponseEntity<>(billService.getAllLegislation(), HttpStatus.OK);
    }
    //TODO delete eventually
    @GetMapping("/testDbId")
    public ResponseEntity<Legislation> getAllLegislation(@RequestParam int bill_id) {
        return new ResponseEntity<>(billService.getLegislationById(bill_id), HttpStatus.OK);
    }
}

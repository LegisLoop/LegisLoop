package com.backend.legisloop.controller;

import com.backend.legisloop.model.Bill;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.service.BillService;
import com.mashape.unirest.http.JsonNode;
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
    public ResponseEntity<List<Bill>> getMasterListByState(@RequestParam String state) throws UnirestException {
        return new ResponseEntity<>(billService.getMasterList(state), HttpStatus.OK);
    }

    @GetMapping("/getBill")
    public ResponseEntity<Legislation> getBill(@RequestParam int bill_id, @RequestParam String state) throws UnirestException, URISyntaxException {
        List<Bill> masterList = billService.getMasterList(state);
        Bill billToFind = masterList.stream()
                .filter(bill -> bill.getBill_id() == bill_id)
                .findFirst()
                .orElse(null);
        return new ResponseEntity<>(billService.getBill(billToFind), HttpStatus.OK);
    }
}

package com.backend.legisloop.controller;

import com.backend.legisloop.model.RollCall;
import com.backend.legisloop.repository.RollCallRepository;
import com.backend.legisloop.service.RollCallService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.util.List;

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
public class RollCallController {
	
    private final RollCallService rollCallService;
    private final RollCallRepository rollCallRepository;

    @GetMapping("/getRollCall")
    public ResponseEntity<RollCall> getRollCallByID(@RequestParam int roll_call_id) throws UnirestException {
    	return new ResponseEntity<>(rollCallService.getRollCallByID(roll_call_id), HttpStatus.OK);
    }
    
    @GetMapping("/getRollCallsForLegislation")
    public ResponseEntity<List<RollCall>> getRollCallsForLegislation(@RequestParam int bill_id) throws UnirestException, URISyntaxException {
    	List<RollCall> rollCalls = rollCallService.getRollCallsForLegislation(bill_id);
    	log.info("{}", rollCalls);
    	return new ResponseEntity<>(rollCalls, HttpStatus.OK);
    }
    //TODO delete eventually
    @GetMapping("/testRollCallDb")
    public ResponseEntity<RollCall> testRollCallDb(@RequestParam int roll_call_id) {
        return new ResponseEntity<>(rollCallRepository.getReferenceById(roll_call_id).toModel(), HttpStatus.OK);
    }
}

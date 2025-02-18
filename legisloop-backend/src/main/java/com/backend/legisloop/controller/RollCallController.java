package com.backend.legisloop.controller;

import com.backend.legisloop.model.RollCall;
import com.backend.legisloop.service.RollCallService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RollCallController {
	
    private final RollCallService rollCallService;

    @GetMapping("/getRollCall")
    public ResponseEntity<RollCall> getRollCallByID(@RequestParam int roll_call_id) throws UnirestException {
    	return new ResponseEntity<>(rollCallService.getRollCallByID(roll_call_id), HttpStatus.OK);
    }
    
}

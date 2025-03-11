package com.backend.legisloop.controller;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RollCallEntity;
import com.backend.legisloop.model.RollCall;
import com.backend.legisloop.repository.RollCallRepository;
import com.backend.legisloop.service.RollCallService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rollCall")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class RollCallController {
	
    private final RollCallService rollCallService;
    private final RollCallRepository rollCallRepository;

    @GetMapping("/{rollCallId}")
    public ResponseEntity<RollCall> getRollCallByID(@PathVariable int rollCallId) {
    	return new ResponseEntity<>(rollCallRepository.getReferenceById(rollCallId).toModel(), HttpStatus.OK);
    }

    @GetMapping("/byBillId/{billId}")
    public ResponseEntity<Page<RollCall>> getRollCallsByBillIdPaginated(
            @PathVariable int billId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RollCall> rollCalls = rollCallService.getRollCallsByBillIdPaginated(billId, page, size);
        return ResponseEntity.ok(rollCalls);

    }
}

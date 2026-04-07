package com.backend.legisloop.controller;

import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Vote;
import com.backend.legisloop.model.Event;
import com.backend.legisloop.service.EventService;
import com.backend.legisloop.service.LegislationService;
import com.backend.legisloop.service.VoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final LegislationService legislationService;
    private final EventService eventService;
    private final VoteService voteService;
    
    // Get a person's events bills (paginated)
    @GetMapping("/{personId}/paginated")
    public ResponseEntity<Page<Event>> getEvents(
            @PathVariable int personId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
    	long startTime = System.nanoTime();
    	Page<Event> events = eventService.getEventsForPerson(personId, page, size);
    	long endTime = System.nanoTime();
    	log.info("Pageable took {} ms", (endTime - startTime) / 1000000.0);
        return ResponseEntity.ok(events);
    }
    
    /**
     * This endpoint returns all events for a given person filtered by a date range.
     * Dates should be provided in the format "yyyy-[m]m-[d]d". Both parameters are optional.
     */
    @GetMapping("/{personId}/filter")
    public ResponseEntity<List<Event>> filterEventsByDate(
            @PathVariable int personId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        // Convert date parameters from String to java.sql.Date if provided.
        Date start = (startDate != null && !startDate.isEmpty()) ? Date.valueOf(startDate) : null;
        Date end = (endDate != null && !endDate.isEmpty()) ? Date.valueOf(endDate) : null;

    	long startTime = System.nanoTime();
        List<Event> filteredEvents = eventService.getEventsForPersonWithinRange(personId, start, end);
    	long endTime = System.nanoTime();
    	log.info("Filtered Timeline took {} ms", (endTime - startTime) / 1000000.0);

        return ResponseEntity.ok(filteredEvents);
    }

}
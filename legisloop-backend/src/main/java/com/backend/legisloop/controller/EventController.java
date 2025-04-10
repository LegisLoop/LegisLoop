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
    private final VoteService voteService;
    
    // Get a person's events bills (paginated)
    @GetMapping("/{personId}/paginated")
    public ResponseEntity<Page<Event>> getEvents(
            @PathVariable int personId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        List<Legislation> legislations = legislationService.getLegislationByRepresentativeId(personId);
        List<Vote> votes = voteService.getVotesByRepresentativeId(personId);

        // Merge the two sorted lists into a single list of events.
        List<Event> mergedEvents = EventService.mergeSortedEvents(legislations, votes);

        // Wrap the merged list into a Page.
        Pageable pageable = PageRequest.of(page, size);
        
        // Calculate start and end indexes for the requested page.
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), mergedEvents.size());
        List<Event> pageContent = mergedEvents.subList(start, end);

        Page<Event> eventPage = new PageImpl<>(pageContent, pageable, mergedEvents.size());
        return ResponseEntity.ok(eventPage);
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

        // For simplicity, fetch all events. (For large datasets consider adding filtering to the services.)
        List<Legislation> legislations = legislationService.getLegislationByRepresentativeId(personId);
        List<Vote> votes = voteService.getVotesByRepresentativeId(personId);

        // Merge events from both sources that fall within the provided date range.
        List<Event> filteredEvents = new ArrayList<>();
        for (Legislation leg : legislations) {
        	Event temp = new Event(leg);
            if (EventService.isWithinRange(temp.getDate(), start, end)) {
                filteredEvents.add(temp);
            }
        }
        for (Vote vote : votes) {
        	Event temp = new Event(vote);
            if (EventService.isWithinRange(temp.getDate(), start, end)) {
                filteredEvents.add(temp);
            }
        }

        // Sort the merged list by date descending (newest first)
        filteredEvents.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));

        return ResponseEntity.ok(filteredEvents);
    }

}
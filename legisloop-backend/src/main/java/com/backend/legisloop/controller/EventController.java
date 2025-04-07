package com.backend.legisloop.controller;

import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Vote;
import com.backend.legisloop.model.Event;
import com.backend.legisloop.service.LegislationService;
import com.backend.legisloop.service.VoteService;

import lombok.RequiredArgsConstructor;

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

        Page<Legislation> legislationPage = legislationService.getLegislationByRepresentativeIdPaginated(personId, page, size);
        Page<Vote> votesPage = voteService.getVotesByRepresentativeIdPaginated(personId, page, size);
        
        List<Legislation> legislations = legislationPage.getContent();
        List<Vote> votes = votesPage.getContent();

        // Merge the two sorted lists into a single list of events.
        List<Event> mergedEvents = mergeSortedEvents(legislations, votes, size);

        // Wrap the merged list into a Page.
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = new PageImpl<>(mergedEvents, pageable, mergedEvents.size());
        return ResponseEntity.ok(eventPage);
    }

    private List<Event> mergeSortedEvents(List<Legislation> legislations, List<Vote> votes, int limit) {
        List<Event> merged = new ArrayList<>();
        int i = 0, j = 0;
        
        // Merge until we have reached the desired limit or run out of items.
        while (merged.size() < limit && (i < legislations.size() || j < votes.size())) {
            if (i < legislations.size() && j < votes.size()) {
                Legislation leg = legislations.get(i);
                Vote vote = votes.get(j);
                Date legislationDate = leg.getDateIntroduced() == null ? leg.getStatus_date() : leg.getDateIntroduced();
                // Compare the two dates: if legislation is newer than vote, add it; otherwise add vote.
                if (legislationDate.compareTo(vote.getDate()) > 0) {
                    merged.add(new Event(leg));
                    i++;
                } else {
                    merged.add(new Event(vote));
                    j++;
                }
            } else if (i < legislations.size()) {
                merged.add(new Event(legislations.get(i++)));
            } else {
                merged.add(new Event(votes.get(j++)));
            }
        }
        return merged;
    }

}
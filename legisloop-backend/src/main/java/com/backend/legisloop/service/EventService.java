package com.backend.legisloop.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.legisloop.enums.EventEnum;
import com.backend.legisloop.enums.VotePosition;
import com.backend.legisloop.model.Event;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Vote;
import com.backend.legisloop.repository.EventRepository;
import com.backend.legisloop.repository.EventRepository.EventProjection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository eventRepository;

    /**
     * Helper method to determine if a given eventDate falls within the start and end dates.
     */
    public static boolean isWithinRange(Date eventDate, Date start, Date end) {
        if (eventDate == null) {
            return false;
        }
        if (start != null && eventDate.before(start)) {
            return false;
        }
        if (end != null && eventDate.after(end)) {
            return false;
        }
        return true;
    }

    public static List<Event> mergeSortedEvents(List<Legislation> legislations, List<Vote> votes) {
        List<Event> merged = new ArrayList<>();

        // Convert and add legislations to merged events.
        for (Legislation leg : legislations) {
            Event e = new Event(leg);
            merged.add(e);
        }
        
        // Convert and add votes to merged events.
        for (Vote vote : votes) {
            merged.add(new Event(vote));
        }

        // Sort the merged list by date descending (newest first)
        merged.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));
        
        return merged;
    }
    
    /**
     * Fetch all events for a person within [start..end], sorted desc.
     */
     public List<Event> getEventsForPersonWithinRange(int personId, Date start, Date end) {
    	 return eventRepository
    			 .findAllEventsByPersonAndDateRange(personId, start, end)
    			 .stream()
    			 .map(this::toEventModel)
    			 .toList();
     }

    /**
     * Fetch a single page of merged events (sponsor‐bills + votes) for a person,
     * sorted by date desc, using a native UNION query under the hood.
     */
    public Page<Event> getEventsForPerson(int personId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository
        		.findAllEventsByPerson(personId, pageable)    // DB now returns exactly 'size' rows at offset 'page'
        		.map(this::toEventModel);                     // map projection → your Event DTO
    }

    private Event toEventModel(EventProjection p) {
        Event e = Event.builder()
                .bill_id(p.getBillId())
                .date(p.getEventDate())
                .event_title(p.getTitle())
                .event_description(p.getDescription())
                .type(EventEnum.valueOf(p.getType()))
                .vote_position(p.getVotePosition() == null
                    ? null
                    : VotePosition.fromVoteID(p.getVotePosition()))
                .build();
        return e;
    }

}

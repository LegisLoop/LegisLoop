package com.backend.legisloop.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.backend.legisloop.model.Event;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Vote;

public class EventService {

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

}

package com.backend.legisloop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

import com.backend.legisloop.enums.EventEnum;
import com.backend.legisloop.enums.VotePosition;

@AllArgsConstructor
@Builder
@Data
public class Event { // Not stored in the DB

	private int bill_id;
    private Date date;
    private String event_title;
    private String event_description;
    private EventEnum type;
    private VotePosition vote_position = null;
    
    public Event(Legislation legislation) {
    	this.bill_id = legislation.getBill_id();
        this.date = legislation.getDateIntroduced() == null ? legislation.getStatus_date() : legislation.getDateIntroduced();
        this.event_title = legislation.getTitle();
        this.event_description = legislation.getDescription();
        this.type = EventEnum.SPONSORED;
        this.vote_position = null;
    }
    
    public Event(Vote vote) {
    	this.bill_id = vote.getBill_id();
        this.date = vote.getDate();
        this.event_title = vote.getBill_title();
        this.event_description = vote.getDescription();
        this.type = EventEnum.VOTE;
        this.vote_position = vote.getVote_position();
    }
}

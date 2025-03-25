package com.backend.legisloop.model;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RollCallEntity;
import com.backend.legisloop.enums.VotePosition;
import com.backend.legisloop.serial.BooleanSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class RollCall {

	private int roll_call_id;
    private int bill_id;
    private Date date;
    private String desc;
    private int yea;
    private int nay;
    private int nv;
    private int absent;
    private int total;
    private boolean passed;
    private List<Vote> votes;

    public RollCallEntity toEntity() {
        return RollCallEntity.builder()
                .roll_call_id(this.roll_call_id)
                .legislation(LegislationEntity.builder().bill_id(this.bill_id).build())
                .date(this.date)
                .desc(this.desc)
                .yea(this.yea)
                .nay(this.nay)
                .nv(this.nv)
                .absent(this.absent)
                .total(this.total)
                .passed(this.passed)
                .votes(this.votes != null ? this.votes.stream().map(Vote::toEntity).toList() : new ArrayList<>())
                .build();
    }
    
    /**
     * Fill a {@link RollCall} object from a LegiScan JSON object.
     * @param obj
     * @return the filled {@link RollCall}
     */
    public static RollCall fillRecord(JsonObject obj) {

    	BooleanSerializer serializer = new BooleanSerializer();
        Gson gson = new GsonBuilder()
        		.setDateFormat("yyyy-mm-dd")
        		.registerTypeAdapter(boolean.class, serializer)
        		.registerTypeAdapter(Boolean.class, serializer)
        		.create();
        RollCall rollCall = gson.fromJson(obj, RollCall.class);

    	List<Vote> votes = new ArrayList<Vote>();
    	JsonArray rollCallVotes = obj.getAsJsonArray("votes");
    	if (rollCallVotes != null) {
	    	rollCallVotes.forEach(rollCallVote -> {
	    		Vote vote = Vote.builder()
	    				.roll_call_id(rollCall.getRoll_call_id())
	    				.person_id(rollCallVote.getAsJsonObject().get("people_id").getAsInt())
	    				.vote_position(VotePosition.fromVoteID(rollCallVote.getAsJsonObject().get("vote_id").getAsInt()))
	    				.build();
	
	    		votes.add(vote);
	    	});
    	}
    	rollCall.setVotes(votes);
    	
    	return rollCall;
    }
}

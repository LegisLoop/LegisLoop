package com.backend.legisloop.service;

import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.RollCall;
import com.backend.legisloop.model.Vote;
import com.backend.legisloop.Utils;
import com.backend.legisloop.enums.VotePosition;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
@Slf4j
public class RollCallService {

    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;

    public RollCall getRollCallByID(int roll_call_id) throws UnirestException {

        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getRollCall")
                .queryString("id", roll_call_id)
                .asJson();

        if (response.getStatus() == 200) {
            try {
            	JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                Utils.checkLegiscanResponseStatus(jsonObject);

                JsonObject rollCallObject = jsonObject.getAsJsonObject("roll_call");

                return fillRecord(rollCallObject);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch bills, server responded with status: " + response.getStatus());
        }
    }
    
    /**
     * Fill a {@link RollCall} object from a LegiScan JSON object.
     * @param obj
     * @return the filled {@link RollCall}
     */
    private static RollCall fillRecord(JsonObject obj) {
    	RollCall rollCall = RollCall.builder()
    			.roll_call_id(obj.get("roll_call_id").getAsInt())
    			.bill_id(obj.get("bill_id").getAsInt())
    			.date(Date.valueOf(obj.get("date").getAsString()))
    			.description(obj.get("desc").getAsString())
    			.yea(obj.get("yea").getAsInt())
    			.nay(obj.get("nay").getAsInt())
    			.nv(obj.get("nv").getAsInt())
    			.absent(obj.get("absent").getAsInt())
    			.total(obj.get("total").getAsInt())
    			.passed(obj.get("passed").getAsBoolean())
    			.build();

    	List<Vote> votes = new ArrayList<Vote>();
    	JsonArray rollCallVotes = obj.getAsJsonArray("votes");
    	rollCallVotes.forEach(rollCallVote -> {
    		Vote vote = Vote.builder()
    				.bill_id(rollCall.getBill_id())
    				.roll_call_id(rollCall.getRoll_call_id())
    				.person_id(rollCallVote.getAsJsonObject().get("people_id").getAsInt())
    				.vote_position(VotePosition.fromVoteID(rollCallVote.getAsJsonObject().get("vote_id").getAsInt()))
    				.build();

    		votes.add(vote);
    	});
    	rollCall.setVotes(votes);
    	
    	return rollCall;
    }
    
}

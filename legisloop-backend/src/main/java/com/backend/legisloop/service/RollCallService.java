package com.backend.legisloop.service;

import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.RollCall;
import com.backend.legisloop.model.Vote;
import com.backend.legisloop.serial.BooleanSerializer;
import com.backend.legisloop.util.Utils;
import com.backend.legisloop.enums.VotePosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RollCallService {

    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;
    
    private final BillService billService;

    public RollCallService(BillService billService) {
        this.billService = billService;
    }

    /**
     * Get the Roll Call from the LegiScan API using it's ID
     * @param roll_call_id
     * @return
     * @throws UnirestException
     */
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
    
    
    public List<RollCall> getRollCallsForLegislation(Legislation legislation) throws UnirestException, URISyntaxException {
		Legislation bill = billService.getBill(legislation);
		return bill.getVotes();
    }
    
    public List<RollCall> getRollCallsForLegislation(int bill_id) throws UnirestException, URISyntaxException {
    	Legislation temp = new Legislation();
    	temp.setBill_id(bill_id);
    	return getRollCallsForLegislation(temp);
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

        log.info("{}", rollCall.toString());

    	List<Vote> votes = new ArrayList<Vote>();
    	JsonArray rollCallVotes = obj.getAsJsonArray("votes");
    	if (rollCallVotes != null) {
	    	rollCallVotes.forEach(rollCallVote -> {
	    		Vote vote = Vote.builder()
	    				.bill_id(rollCall.getBill_id())
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

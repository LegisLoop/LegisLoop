package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RollCallEntity;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.RollCall;
import com.backend.legisloop.model.Vote;
import com.backend.legisloop.repository.LegislationDocumentRepository;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RollCallRepository;
import com.backend.legisloop.serial.BooleanSerializer;
import com.backend.legisloop.util.Utils;
import com.backend.legisloop.entities.RollCallEntity;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RollCallService {

    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;
    
    private final BillService billService;

    private final LegislationRepository legislationRepository;
    private final RollCallRepository rollCallRepository;

    /**
     * Get the Roll Call from the LegiScan API using it's ID
     * @param roll_call_id
     * @return
     * @apiNote LegiScan called.
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

                return RollCall.fillRecord(rollCallObject);
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
     * For a piece of {@link Legislation}, get the server-side roll calls
     * @param legislation
     * @return The List of RollCalls
     * @apiNote LegiScan called.
     * @throws UnirestException
     * @throws URISyntaxException
     */
    public List<RollCall> getRollCallsForLegislation(Legislation legislation) throws UnirestException, URISyntaxException {
		Legislation bill = billService.getBill(legislation);
		return bill.getRoll_calls();
    }
    
    /**
     * For a piece of {@link Legislation}, get the server-side roll calls
     * @param bill_id the LegiScan bill_id
     * @return The List of RollCalls
     * @apiNote LegiScan called.
     * @throws UnirestException
     * @throws URISyntaxException
     */
    public List<RollCall> getRollCallsForLegislation(int bill_id) throws UnirestException, URISyntaxException {
    	Legislation temp = new Legislation();
    	temp.setBill_id(bill_id);
    	return getRollCallsForLegislation(temp);
    }
    
    public Page<RollCall> getRollCallsByBillIdPaginated(int billId, int page, int size) {
        LegislationEntity legislation = legislationRepository.findById(billId)
                .orElseThrow(() -> new EntityNotFoundException("Legislation not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<RollCallEntity> rollCallEntities = rollCallRepository.findByLegislation(legislation, pageable);

        return rollCallEntities.map(RollCallEntity::toModel);
    }
}

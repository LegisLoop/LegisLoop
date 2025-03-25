package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Type;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepresentativeService {
	
	private final RepresentativeRepository representativeRepository;
    private final LegislationRepository legislationRepository;

    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;

    /**
     * Get all the people who have participated in this session.
     * @param sessionId The LegiScan session_id
     * @return All representatives from the session.
     * @throws UnirestException
     * @apiNote LegiScan called.
     */
    public List<Representative> getSessionPeople(int sessionId) throws UnirestException {

        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getSessionPeople")
                .queryString("id", sessionId)
                .asJson();

        if (response.getStatus() == 200) {
            try {
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                JsonArray peopleArray = jsonObject.getAsJsonObject("sessionpeople").getAsJsonArray("people");

                return fillRecords(peopleArray);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch bills, server responded with status: " + response.getStatus());
        }
    }

    /**
     * Get the most up-to-date version of a legislator.
     * @param personId The LegiScan person_id 
     * @return The Representative
     * @throws UnirestException
     * @apiNote LegiScan called.
     */
    public Representative getRepresentativeById(int personId) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getPerson")
                .queryString("id", personId)
                .asJson();

        if (response.getStatus() == 200) {
            try {
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                JsonObject person = jsonObject.getAsJsonObject("person");

                return fillRecord(person);
            }
            catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch bills, server responded with status: " + response.getStatus());
        }
    }

    /**
     * Get every bill that a representative has sponsored.
     * @param personId The LegiScan person_id 
     * @return A list of {@link Legislation} stubs
     * @throws UnirestException
     * @apiNote LegiScan called.
     */
    public List<Legislation> getRepresentativeBills(int personId) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getSponsoredList")
                .queryString("id", personId)
                .asJson();

        if (response.getStatus() == 200) {
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                JsonArray listList = jsonObject.getAsJsonObject("sponsoredbills").getAsJsonArray("bills");

                Type listType = new TypeToken<List<Legislation>>() {}.getType();
                return gson.fromJson(listList, listType);
            }
            catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch bills, server responded with status: " + response.getStatus());
        }
    }

    public List<Representative> getSponsorsByBillId(int billId) {
        LegislationEntity bill = legislationRepository.findById(billId)
                .orElseThrow(() -> new EntityNotFoundException("Legislation not found"));

        return representativeRepository.findBySponsoredBills(bill).stream().map(RepresentativeEntity::toModel).toList();
    }

    public List<Representative> searchRepresentatives(String keyword) {
        return representativeRepository.searchByName(keyword).stream().map(RepresentativeEntity::toModel).toList();
    }
    
    /**
     * Save the {@link Representative} to the DB, if it has changed.
     * @param newer The newer {@link Representative} to check
     * @param older The original {@link Representative} to check, presumably from our database.
     * @return The newer Representative
     * @throws UnirestException
     */
    public Representative update(Representative newer, Representative older) throws UnirestException {
    	if (newer.getPerson_hash() != older.getPerson_hash()) {	// Something has changed.
    		representativeRepository.saveAndFlush(newer.toEntity());
		}
		
		return newer;
    }
    
    /**
     * Take an object of a person and return the Representative using Gson
     * @param personObject The JSON Object from the LegiScan API
     * @return The representative
     */
    public static Representative fillRecord(JsonObject personObject) {
        try {
        	Gson gson = new Gson();
        	return gson.fromJson(personObject, Representative.class);
    	} catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
    
    /**
     * Take an array of people and return the Representatives using Gson
     * @param peopleArray The array from the LegiScan API
     * @return The representatives
     */
    public static List<Representative> fillRecords(JsonArray peopleArray) {
    	try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Representative>>() {}.getType();
            return gson.fromJson(peopleArray, listType);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}

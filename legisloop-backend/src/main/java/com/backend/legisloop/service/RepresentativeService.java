package com.backend.legisloop.service;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
                Gson gson = new Gson();
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                JsonArray peopleArray = jsonObject.getAsJsonObject("sessionpeople").getAsJsonArray("people");

                Type listType = new TypeToken<List<Representative>>() {}.getType();
                return gson.fromJson(peopleArray, listType);
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
                Gson gson = new Gson();
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                JsonObject person = jsonObject.getAsJsonObject("person");

                return gson.fromJson(person, Representative.class);
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
    
    /**
     * From the provided {@link Representative}, fetch the one upstream and update the database if
     * it's new.
     * @param rep {@link Representative} to check
     * @return The most up to date {@link Representative}.
     * @throws UnirestException
     * @apiNote LegiScan called.
     */
    public Representative update(Representative rep) throws UnirestException {
    	Representative representativeFresh = getRepresentativeById(rep.getPeople_id());
    	if (representativeFresh.getPerson_hash() == rep.getPerson_hash()) {	// Nothing has changed.
			return rep;
		}
		
		log.debug("We have person_id {} with person_hash {}, but upstream person_hash is {}", 
				rep.getPerson_hash(), rep.getPerson_hash(), representativeFresh.getPerson_hash());
		representativeRepository.save(representativeFresh.toEntity());
		
		return representativeFresh;
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
    		representativeRepository.save(newer.toEntity());
		}
		
		return newer;
    }
}

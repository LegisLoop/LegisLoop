package com.backend.legisloop.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class SummaryService {

    @Value("${readeasy.api.key}")
    private String API_KEY;
    @Value("${readeasy.base.url}")
    private String url;

    public String getSummaryOfContent(String query) throws UnirestException {

        log.info("Fetching summary about {}...", query.substring(0, query.length() > 20 ? 20 : query.length()));
        
    	JSONObject jsonBody = new JSONObject();
        jsonBody.put("text", query);

        HttpResponse<String> response = Unirest.post(url + "/text/showHighlights")
            .header("X-API-KEY", API_KEY)
            .header("Content-Language", "en")
            .header("Content-Type", "application/json")
            .body(jsonBody.toString())
            .asString();

        if (response.getStatus() == 200) {
            try{
                return response.getBody();
            } catch (Exception e){
                log.error(e.getMessage());
                throw e;
            }
        }
        else {
        	log.error("Failed to fetch summary, server responded with status: " + response.getStatus() + " and content:\n\t" + response.getBody());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch summary, server responded with status: " + response.getStatus() + " and content:\n\t" + response.getBody());
        }
    }
    
    public String getSummaryOfContentByAge(String query, int age) throws UnirestException {
    	if (age < 5 || age > 21) throw new IllegalArgumentException("Age out of range");

        log.info("Fetching summary for {} year olds about {}...", age, query.substring(0, query.length() > 20 ? 20 : query.length()));
        
    	JSONObject jsonBody = new JSONObject();
        jsonBody.put("text", query);
        jsonBody.put("age", age);

        HttpResponse<String> response = Unirest.post(url + "/text/makeReadable")
            .header("X-API-KEY", API_KEY)
            .header("Content-Language", "en")
            .header("Content-Type", "application/json")
            .body(jsonBody.toString())
            .asString();

        if (response.getStatus() == 200) {
            try{
                return response.getBody();
            } catch (Exception e){
                log.error(e.getMessage());
                throw e;
            }
        }
        else {
        	log.error("Failed to fetch summary, server responded with status: " + response.getStatus() + " and content:\n\t" + response.getBody());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch summary, server responded with status: " + response.getStatus() + " and content:\n\t" + response.getBody());
        }
    }
}

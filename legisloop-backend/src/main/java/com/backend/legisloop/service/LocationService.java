package com.backend.legisloop.service;

import com.backend.legisloop.enums.StateEnum;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LocationService {

    @Value("${location.iq.api.key}")
    private String API_KEY;

    @Value("${location.iq.base.url}")
    private String BASE_URL;

    public StateEnum getUserLocation(double latitude, double longitude) throws UnirestException {

        HttpResponse<JsonNode> response = Unirest.get(BASE_URL)
                .queryString("key", API_KEY)
                .queryString("lat", latitude)
                .queryString("lon", longitude)
                .queryString("format", "json")
                .queryString("normalizeaddress", 1) // makes data a bit easier to parse
                .queryString("statecode", 1) // makes the response include state abbreviation (nj, ca, etc.)
                .asJson();

        if (response.getStatus() == 200) {
            JSONObject jsonObject = response.getBody().getObject();
            String stateCode = jsonObject.getJSONObject("address").getString("state_code");

            return StateEnum.valueOf(stateCode.toUpperCase());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch location, server responded with status: " + response.getStatus());
        }
    }
}

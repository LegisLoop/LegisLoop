package com.backend.legisloop.service;


import com.backend.legisloop.model.LegiscanDataset;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Type;
import java.util.List;

@Service
@Slf4j
public class InitializationService {

    @Value("${legiscan.api.key}")
    private String API_KEY;
    @Value("${legiscan.base.url}")
    private String url;

    public List<LegiscanDataset> getDatasetListByState(String state) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(url + "/")
                .queryString("key", API_KEY)
                .queryString("op", "getDatasetList")
                .queryString("state", state)
                .asJson();

        if (response.getStatus() == 200) {
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = JsonParser.parseString(response.getBody().toString()).getAsJsonObject();
                checkLegiscanResponseStatus(jsonObject);

                JsonArray datasetList = jsonObject.get("datasetlist").getAsJsonArray();
                Type listType = new TypeToken<List<LegiscanDataset>>() {}.getType();
                return gson.fromJson(datasetList, listType);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch bills, server responded with status: " + response.getStatus());
        }
    }

    private void checkLegiscanResponseStatus(JsonObject response) {
        if (response.has("status") && "ERROR".equals(response.get("status").getAsString())) {
            String errorMessage = response.getAsJsonObject("alert").get("message").getAsString();
            log.error("API Error: {}", errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "API Error: " + errorMessage);
        }
    }
}

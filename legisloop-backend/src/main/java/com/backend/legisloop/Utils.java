package com.backend.legisloop;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {

	public static void checkLegiscanResponseStatus(JsonObject response) {
        if (response.has("status") && "ERROR".equals(response.get("status").getAsString())) {
            String errorMessage = response.getAsJsonObject("alert").get("message").getAsString();
            log.error("API Error: {}", errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "API Error: " + errorMessage);
        }
    }

}

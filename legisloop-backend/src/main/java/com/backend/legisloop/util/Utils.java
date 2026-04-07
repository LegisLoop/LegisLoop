package com.backend.legisloop.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

	public static void checkLegiscanResponseStatus(JsonObject response) {
        if (response.has("status") && "ERROR".equals(response.get("status").getAsString())) {
        	if (response.getAsJsonObject("alert") == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "API Error: Unknown");
        	if (response.getAsJsonObject("alert").get("message") == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "API Error: Unknown");
            String errorMessage = response.getAsJsonObject("alert").get("message").getAsString();
            log.error("API Error: {}", errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "API Error: " + errorMessage);
        }
    }

}

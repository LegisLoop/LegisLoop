package com.backend.legisloop;

import com.backend.legisloop.util.Utils;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void testCheckLegiscanResponseStatus_ValidResponse_NoError() {
        // Valid response without an error
        JsonObject validResponse = new JsonObject();
        validResponse.addProperty("status", "OK");

        // Should not throw an exception
        assertDoesNotThrow(() -> Utils.checkLegiscanResponseStatus(validResponse));
    }

    @Test
    void testCheckLegiscanResponseStatus_ErrorResponse_WithMessage() {
        // Simulate an error response from LegiScan API
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("status", "ERROR");

        JsonObject alert = new JsonObject();
        alert.addProperty("message", "Invalid Request");
        errorResponse.add("alert", alert);

        // Verify exception is thrown with correct status and message
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            Utils.checkLegiscanResponseStatus(errorResponse);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("API Error: Invalid Request", exception.getReason());
    }

    @Test
    void testCheckLegiscanResponseStatus_ErrorResponse_MissingAlert() {
        // Simulate an error response where "alert" object is missing
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("status", "ERROR");

        // Verify exception is thrown with correct status and message
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            Utils.checkLegiscanResponseStatus(errorResponse);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("API Error: Unknown", exception.getReason());
    }

    @Test
    void testCheckLegiscanResponseStatus_ErrorResponse_MissingMessage() {
        // Simulate an error response where "message" field is missing
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("status", "ERROR");

        JsonObject alert = new JsonObject(); // No "message" field
        errorResponse.add("alert", alert);

        // Verify exception is thrown with correct status and message
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            Utils.checkLegiscanResponseStatus(errorResponse);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("API Error: Unknown", exception.getReason());
    }
}

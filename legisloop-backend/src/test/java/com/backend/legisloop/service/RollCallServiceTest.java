package com.backend.legisloop.service;

import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.RollCall;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RollCallServiceTest {

    @Mock
    private BillService billService;

    @InjectMocks
    private RollCallService rollCallService;

    @BeforeEach
    void setUp() {
        // No setup needed, as @InjectMocks initializes RollCallService
    }

    @Test
    void testGetRollCallByID_Success() throws UnirestException {
        // Mock API response
        HttpResponse<JsonNode> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(new JsonNode("{ \"roll_call\": { \"roll_call_id\": 123, \"votes\": [] } }"));

        // Mock Unirest call
        try (var unirestMockedStatic = Mockito.mockStatic(com.mashape.unirest.http.Unirest.class)) {
            unirestMockedStatic.when(() -> Unirest.get(anyString())).thenReturn(mockResponse);

            // Call the service method
            RollCall rollCall = rollCallService.getRollCallByID(123);

            // Verify the result
            assertNotNull(rollCall);
            assertEquals(123, rollCall.getRoll_call_id());
        }
    }

    @Test
    void testGetRollCallByID_Failure() throws UnirestException {
        // Mock API response with bad request
        HttpResponse<JsonNode> mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(400);

        try (var unirestMockedStatic = Mockito.mockStatic(com.mashape.unirest.http.Unirest.class)) {
            unirestMockedStatic.when(() -> Unirest.get(anyString())).thenReturn(mockResponse);

            // Expect exception
            assertThrows(ResponseStatusException.class, () -> rollCallService.getRollCallByID(123));
        }
    }

    @Test
    void testGetRollCallsForLegislation() throws UnirestException, URISyntaxException {
        // Mock bill service response
        Legislation mockLegislation = new Legislation();
        mockLegislation.setBill_id(123);
        mockLegislation.setVotes(Collections.emptyList());

        when(billService.getBill(any(Legislation.class))).thenReturn(mockLegislation);

        List<RollCall> rollCalls = rollCallService.getRollCallsForLegislation(123);

        assertNotNull(rollCalls);
        assertTrue(rollCalls.isEmpty());
        verify(billService, times(1)).getBill(any(Legislation.class));
    }
}
	
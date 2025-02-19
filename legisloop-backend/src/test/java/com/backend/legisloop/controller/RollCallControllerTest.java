package com.backend.legisloop.controller;

import com.backend.legisloop.model.RollCall;
import com.backend.legisloop.repository.UserRepository;
import com.backend.legisloop.service.RollCallService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RollCallController.class)
class RollCallControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RollCallService rollCallService;
    
    @MockBean
    private UserRepository userRepository; // Needed for any repository. Source: The Oracle ChatGPT

    @Test
    void testGetRollCallByID_Success() throws Exception {
        // Mock service response
        RollCall mockRollCall = RollCall.builder().build();
        mockRollCall.setRoll_call_id(123);

        when(rollCallService.getRollCallByID(anyInt())).thenReturn(mockRollCall);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getRollCall")
                        .param("roll_call_id", "123"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roll_call_id").value(123));
    }

    @Test
    void testGetRollCallByID_Failure() throws Exception {
        // Mock exception
        when(rollCallService.getRollCallByID(anyInt())).thenThrow(new UnirestException("API Error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getRollCall")
                        .param("roll_call_id", "123"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void testGetRollCallsForLegislation_Success() throws Exception {
        // Mock service response
        List<RollCall> mockRollCalls = Collections.singletonList(RollCall.builder().build());

        when(rollCallService.getRollCallsForLegislation(anyInt())).thenReturn(mockRollCalls);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getRollCallsForLegislation")
                        .param("bill_id", "456"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
    }

    @Test
    void testGetRollCallsForLegislation_Failure() throws Exception {
        // Mock exception
        when(rollCallService.getRollCallsForLegislation(anyInt())).thenThrow(new URISyntaxException("Invalid URI", "error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getRollCallsForLegislation")
                        .param("bill_id", "456"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}

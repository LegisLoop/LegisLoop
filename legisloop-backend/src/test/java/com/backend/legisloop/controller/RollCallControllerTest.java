package com.backend.legisloop.controller;

import com.backend.legisloop.model.RollCall;
import com.backend.legisloop.repository.RollCallRepository;
import com.backend.legisloop.service.RollCallService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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
    private RollCallRepository rollCallReposity; // Needed for any repository. Source: The Oracle ChatGPT

    @Test
    void testGetRollCallByID_Success() throws Exception {
        // Mock service response
        RollCall mockRollCall = RollCall.builder().build();
        mockRollCall.setRoll_call_id(123);

        when(rollCallService.getRollCallByID_DB(anyInt())).thenReturn(mockRollCall);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rollCall/123"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roll_call_id").value(123));
    }

    @Test
    void testGetRollCallByID_Failure() throws Exception {
        // Mock exception
        when(rollCallService.getRollCallByID(anyInt())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "API Error: "));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getRollCall")
                        .param("roll_call_id", "123"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testGetRollCallsByBillIdPaginated_Success() throws Exception {
        int billId = 456;
        int page = 0;
        int size = 10;

        // Mock response: a page with 1 roll call
        RollCall mockRollCall = RollCall.builder().roll_call_id(1).build();
        Page<RollCall> mockPage = new PageImpl<>(List.of(mockRollCall));

        when(rollCallService.getRollCallsByBillIdPaginated(eq(billId), eq(page), eq(size)))
                .thenReturn(mockPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rollCall/byBillId/" + billId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roll_call_id").value(1));
    }

    @Test
    void testGetRollCallsForLegislation_Failure() throws Exception {
        // Mock exception
        when(rollCallService.getRollCallsForLegislation(anyInt())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "API Error: "));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/getRollCallsForLegislation")
                        .param("bill_id", "456"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}

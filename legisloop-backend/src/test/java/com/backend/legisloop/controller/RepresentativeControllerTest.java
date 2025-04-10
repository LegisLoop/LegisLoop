package com.backend.legisloop.controller;

import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.service.RepresentativeService;
import com.backend.legisloop.repository.RepresentativeRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepresentativeController.class)
class RepresentativeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepresentativeService representativeService;

    @MockBean
    private RepresentativeRepository representativeRepository; // Needed for any repository involved. Source: The Oracle ChatGPT

    // Test for GET /api/v1/representative/getSessionPeople
    @Test
    void testGetSessionPeople_success() throws Exception {
        // Arrange: Create two dummy representatives.
        Representative rep1 = new Representative(); // populate fields if needed
        Representative rep2 = new Representative();
        List<Representative> reps = Arrays.asList(rep1, rep2);
        when(representativeService.getSessionPeople(1)).thenReturn(reps);

        // Act & Assert: Call the endpoint with sessionId=1.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/representative/getSessionPeople")
                .param("sessionId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // Test for GET /api/v1/representative/{personId}
    @Test
    void testGetPersonById_success() throws Exception {
        // Arrange: Create a dummy RepresentativeEntity and stub its toModel() conversion.
        Representative rep = new Representative();
        RepresentativeEntity repEntity = org.mockito.Mockito.mock(RepresentativeEntity.class);
        when(repEntity.toModel()).thenReturn(rep);
        when(representativeRepository.getReferenceById(1)).thenReturn(repEntity);

        // Act & Assert: Call the endpoint with personId=1.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/representative/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    // Test for GET /api/v1/representative/sponsorsOf/{billId}
    @Test
    void testGetSponsorsByBill_success() throws Exception {
        // Arrange: Create a dummy representative list.
        Representative rep = new Representative();
        List<Representative> sponsors = Arrays.asList(rep);
        when(representativeService.getSponsorsByBillId(100)).thenReturn(sponsors);

        // Act & Assert: Call the endpoint with billId=100.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/representative/sponsorsOf/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // Test for GET /api/v1/representative/stateId/{stateId}
    @Test
    void testGetRepresentativeByStateId_success() throws Exception {
        // Arrange: Create a dummy RepresentativeEntity and stub its toModel() conversion.
        Representative rep = new Representative();
        RepresentativeEntity repEntity = org.mockito.Mockito.mock(RepresentativeEntity.class);
        when(repEntity.toModel()).thenReturn(rep);
        when(representativeRepository.findByStateId(5)).thenReturn(Arrays.asList(repEntity));

        // Act & Assert: Call the endpoint with stateId=5.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/representative/stateId/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
}

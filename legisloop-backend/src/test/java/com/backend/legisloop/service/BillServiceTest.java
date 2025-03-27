package com.backend.legisloop.service;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private LegislationRepository legislationRepository;

    @Mock
    private RepresentativeRepository representativeRepository;

    @InjectMocks
    private BillService billService;

    @Test
    void testGetLegislationByState() {
        StateEnum state = StateEnum.NJ;
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        List<LegislationEntity> mockEntities = List.of(
                createMockLegislationEntity(),
                createMockLegislationEntity()
        );
        Page<LegislationEntity> mockPage = new PageImpl<>(mockEntities, pageable, mockEntities.size());

        when(legislationRepository.findByStateOrderByStatusDateDesc(state, pageable)).thenReturn(mockPage);

        Page<Legislation> result = billService.getLegislationByState(state, page, size);

        assertEquals(2, result.getContent().size());
        verify(legislationRepository).findByStateOrderByStatusDateDesc(state, pageable);
    }

    @Test
    void testGetLegislationByStateId() {
        int stateId = 34;
        StateEnum state = StateEnum.fromStateID(stateId);
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        List<LegislationEntity> mockEntities = List.of(
                createMockLegislationEntity(),
                createMockLegislationEntity()
        );
        Page<LegislationEntity> mockPage = new PageImpl<>(mockEntities, pageable, mockEntities.size());

        when(legislationRepository.findByStateOrderByStatusDateDesc(state, pageable)).thenReturn(mockPage);

        Page<Legislation> result = billService.getLegislationByStateId(stateId, page, size);

        assertEquals(2, result.getContent().size());
        verify(legislationRepository).findByStateOrderByStatusDateDesc(state, pageable);
    }

    @Test
    void testGetLegislationByRepresentativeIdPaginated() {
        int repId = 101;
        RepresentativeEntity rep = new RepresentativeEntity();
        int page = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(page, size);

        List<LegislationEntity> mockEntities = List.of(
                createMockLegislationEntity()
        );
        Page<LegislationEntity> mockPage = new PageImpl<>(mockEntities, pageable, size);

        when(representativeRepository.findById(repId)).thenReturn(Optional.of(rep));
        when(legislationRepository.findBySponsorsOrderByStatusDateDesc(rep, pageable)).thenReturn(mockPage);

        Page<Legislation> result = billService.getLegislationByRepresentativeIdPaginated(repId, page, size);

        assertEquals(1, result.getContent().size());
        verify(representativeRepository).findById(repId);
        verify(legislationRepository).findBySponsorsOrderByStatusDateDesc(rep, pageable);
    }

    @Test
    void testGetLegislationByRepresentativeIdPaginated_notFound() {
        int repId = 202;
        when(representativeRepository.findById(repId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                billService.getLegislationByRepresentativeIdPaginated(repId, 0, 5));

        assertEquals("Representative not found", exception.getMessage());
    }

    @Test
    void testGetLegislationBySessionIdPaginated() {
        int sessionId = 99;
        int page = 0;
        int size = 3;
        Pageable pageable = PageRequest.of(page, size);

        List<LegislationEntity> mockEntities = List.of(
                createMockLegislationEntity(),
                createMockLegislationEntity(),
                createMockLegislationEntity()
        );
        Page<LegislationEntity> mockPage = new PageImpl<>(mockEntities, pageable, mockEntities.size());

        when(legislationRepository.findBySessionId(sessionId, pageable)).thenReturn(mockPage);

        Page<Legislation> result = billService.getLegislationBySessionIdPaginated(sessionId, page, size);

        assertEquals(3, result.getContent().size());
        verify(legislationRepository).findBySessionId(sessionId, pageable);
    }
    private LegislationEntity createMockLegislationEntity() {
        return LegislationEntity.builder()
                .bill_id(1)
                .session_id(100)
                .title("Sample Title")
                .description("Sample Description")
                .summary("Sample Summary")
                .change_hash("abc123")
                .url("https://example.com/bill/1")
                .state_link("https://state.example.com/bill/1")
                .state(StateEnum.NJ)
                .documents(new ArrayList<>())
                .sponsors(new ArrayList<>())
                .endorsements(new ArrayList<>())
                .rollCalls(new ArrayList<>())
                .build();
    }
}
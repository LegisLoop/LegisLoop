package com.backend.legisloop.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.backend.legisloop.service.BillService;
import com.backend.legisloop.service.RepresentativeService;

@ExtendWith(MockitoExtension.class)
class UpdateDatabaseTest {

	@Mock
	private LegislationRepository legislationRepository;

	@Mock
	private BillService billService;
	
	@Mock
	private RepresentativeRepository representativeRepository;

	@Mock
	private RepresentativeService representativeService;

	@InjectMocks
	private UpdateDatabaseTasks updateDatabaseTasks;

	private Legislation testLegislation;
	private LegislationEntity testEntity;

	@BeforeEach
	void setUp() {
		testLegislation = new Legislation();
		testLegislation.setBill_id(1);
		testLegislation.setChange_hash("hash1");

		testEntity = new LegislationEntity();
		testEntity.setBill_id(1);
		testEntity.setChange_hash("hash1");
	}

	@Test
	void whenNewBillExists_shouldSaveIt() throws Exception {
		// Given
		when(billService.getMasterListChange(any())).thenReturn(Arrays.asList(testLegislation));
		when(legislationRepository.findById(1)).thenReturn(Optional.empty());
		when(billService.getBill(testLegislation)).thenReturn(testLegislation);

		// When
		updateDatabaseTasks.updateLegislation();

		// Then
		verify(legislationRepository, times(StateEnum.values().length)).save(any());
	}

	@Test
	void whenBillChangedHash_shouldUpdateIt() throws Exception {
		// Given
		testEntity.setChange_hash("oldHash");
		when(billService.getMasterListChange(any())).thenReturn(Arrays.asList(testLegislation));
		when(legislationRepository.findById(1)).thenReturn(Optional.of(testEntity));
		when(billService.getBill(testLegislation)).thenReturn(testLegislation);

		// When
			updateDatabaseTasks.updateLegislation();

		// Then
		verify(legislationRepository, times(StateEnum.values().length)).save(any());
	}

	@Test
	void whenBillUnchanged_shouldNotUpdate() throws Exception {
		// Given
		when(billService.getMasterListChange(any())).thenReturn(Arrays.asList(testLegislation));
		when(legislationRepository.findById(1)).thenReturn(Optional.of(testEntity));

		// When
		updateDatabaseTasks.updateLegislation();

		// Then
		verify(billService, never()).getBill(any());
		verify(legislationRepository, never()).save(any());
	}

	@Test
	void shouldProcessAllStates() throws Exception {
		// Given
		when(billService.getMasterListChange(any())).thenReturn(Arrays.asList(testLegislation));
		when(legislationRepository.findById(any())).thenReturn(Optional.empty());
		when(billService.getBill(any())).thenReturn(testLegislation);

		// When
		updateDatabaseTasks.updateLegislation();

		// Then
		verify(billService, times(StateEnum.values().length)).getMasterListChange(any());
	}

	@Test
	void whenMultipleBillsExist_shouldProcessAllBills() throws Exception {
		// Given
		Legislation secondLegislation = new Legislation();
		secondLegislation.setBill_id(2);
		secondLegislation.setChange_hash("hash2");

		LegislationEntity secondEntity = new LegislationEntity();
		secondEntity.setBill_id(2);
		secondEntity.setChange_hash("oldHash2");

		when(billService.getMasterListChange(any())).thenReturn(Arrays.asList(testLegislation, secondLegislation));
		when(legislationRepository.findById(1)).thenReturn(Optional.of(testEntity));
		when(legislationRepository.findById(2)).thenReturn(Optional.of(secondEntity));
		when(billService.getBill(secondLegislation)).thenReturn(secondLegislation);

		// When
		updateDatabaseTasks.updateLegislation();

		// Then
		verify(billService, times(StateEnum.values().length)).getBill(any());
		verify(legislationRepository, times(StateEnum.values().length)).save(any());
	}
	
	
	/**
     * Test case: When the representative's person_hash is different from the database version,
     * then the legislator should be updated.
     */
    @Test
    void whenPersonHashChanged_shouldUpdateLegislator() throws Exception {
        // Given a legislator already in the database with an "old" hash.
        RepresentativeEntity dbRep = new RepresentativeEntity();
        dbRep.setPeople_id(1);
        dbRep.setPerson_hash("oldHash");

        // And an upstream representative with a new hash.
        Representative apiRep = new Representative() {
            @Override
            public RepresentativeEntity toEntity() {
                RepresentativeEntity entity = new RepresentativeEntity();
                entity.setPeople_id(this.getPeople_id());
                entity.setPerson_hash(this.getPerson_hash());
                return entity;
            }
        };
        apiRep.setPeople_id(1);
        apiRep.setPerson_hash("newHash");

        when(representativeRepository.findAll()).thenReturn(Arrays.asList(dbRep));
        when(representativeService.getRepresentativeById(1)).thenReturn(apiRep);

        // When
        updateDatabaseTasks.updatePeople();

        // Then: The repository should save the updated representative and flush changes.
        verify(representativeRepository, times(1)).save(any(RepresentativeEntity.class));
        verify(representativeRepository, times(1)).flush();
    }

    /**
     * Test case: When the representative's person_hash is unchanged,
     * then no update should occur.
     */
    @Test
    void whenPersonHashUnchanged_shouldNotUpdateLegislator() throws Exception {
        // Given a legislator with the same hash in the DB.
        RepresentativeEntity dbRep = new RepresentativeEntity();
        dbRep.setPeople_id(1);
        dbRep.setPerson_hash("sameHash");

        // And an upstream representative that returns the same hash.
        Representative apiRep = new Representative() {
            @Override
            public RepresentativeEntity toEntity() {
                RepresentativeEntity entity = new RepresentativeEntity();
                entity.setPeople_id(this.getPeople_id());
                entity.setPerson_hash(this.getPerson_hash());
                return entity;
            }
        };
        apiRep.setPeople_id(1);
        // Using the same literal to ensure reference equality.
        apiRep.setPerson_hash("sameHash");

        when(representativeRepository.findAll()).thenReturn(Arrays.asList(dbRep));
        when(representativeService.getRepresentativeById(1)).thenReturn(apiRep);

        // When
        updateDatabaseTasks.updatePeople();

        // Then: No save should be triggered.
        verify(representativeRepository, never()).save(any(RepresentativeEntity.class));
        verify(representativeRepository, times(1)).flush();
    }

    /**
     * Test case: When processing multiple legislators,
     * only the ones with changed person_hash should be updated.
     */
    @Test
    void whenMultipleLegislators_shouldProcessEachLegislator() throws Exception {
        // Given two legislators in the database.
        RepresentativeEntity dbRep1 = new RepresentativeEntity();
        dbRep1.setPeople_id(1);
        dbRep1.setPerson_hash("old1");

        RepresentativeEntity dbRep2 = new RepresentativeEntity();
        dbRep2.setPeople_id(2);
        dbRep2.setPerson_hash("same");

        // Upstream data: first one is updated, second remains unchanged.
        Representative apiRep1 = new Representative() {
            @Override
            public RepresentativeEntity toEntity() {
                RepresentativeEntity entity = new RepresentativeEntity();
                entity.setPeople_id(this.getPeople_id());
                entity.setPerson_hash(this.getPerson_hash());
                return entity;
            }
        };
        apiRep1.setPeople_id(1);
        apiRep1.setPerson_hash("new1");

        Representative apiRep2 = new Representative() {
            @Override
            public RepresentativeEntity toEntity() {
                RepresentativeEntity entity = new RepresentativeEntity();
                entity.setPeople_id(this.getPeople_id());
                entity.setPerson_hash(this.getPerson_hash());
                return entity;
            }
        };
        apiRep2.setPeople_id(2);
        apiRep2.setPerson_hash("same");

        when(representativeRepository.findAll()).thenReturn(Arrays.asList(dbRep1, dbRep2));
        when(representativeService.getRepresentativeById(1)).thenReturn(apiRep1);
        when(representativeService.getRepresentativeById(2)).thenReturn(apiRep2);

        // When
        updateDatabaseTasks.updatePeople();

        // Then: Only the first legislator should be updated.
        verify(representativeRepository, times(1)).save(any(RepresentativeEntity.class));
        verify(representativeRepository, times(1)).flush();
    }

    /**
     * Test case: When no legislators exist in the repository,
     * the method should simply flush (with zero updates).
     */
    @Test
    void whenNoLegislatorsExist_shouldFlushWithoutSaving() throws Exception {
        // Given an empty list.
        when(representativeRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        updateDatabaseTasks.updatePeople();

        // Then: No save operations, only flush.
        verify(representativeRepository, never()).save(any(RepresentativeEntity.class));
        verify(representativeRepository, times(1)).flush();
    }
}

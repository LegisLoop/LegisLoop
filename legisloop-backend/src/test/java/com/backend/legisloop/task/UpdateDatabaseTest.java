package com.backend.legisloop.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.service.BillService;

@ExtendWith(MockitoExtension.class)
class UpdateDatabaseTest {

	@Mock
	private LegislationRepository legislationRepository;

	@Mock
	private BillService billService;

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
		verify(legislationRepository, times(1)).saveAll(any());
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
		verify(legislationRepository, times(1)).saveAll(any());
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
		verify(legislationRepository, times(1)).saveAll(any());
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
		verify(legislationRepository, times(1)).saveAll(any());
	}
}

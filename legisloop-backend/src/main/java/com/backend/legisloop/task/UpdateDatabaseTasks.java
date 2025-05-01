package com.backend.legisloop.task;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.backend.legisloop.service.InitializationService;
import com.backend.legisloop.service.LegislationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.backend.legisloop.service.RepresentativeService;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateDatabaseTasks {


	private final LegislationRepository legislationRepository;
	private final RepresentativeRepository representativeRepository;
	private final LegislationService billService;
	private final RepresentativeService representativeService;
	private final InitializationService initializationService;
	
	@Scheduled(timeUnit = TimeUnit.MINUTES,
			fixedRate = 60,
			initialDelay = 300)
	public void updateLegislation() throws UnirestException, URISyntaxException, IOException {
		
		log.info("Updating legislation...");
		
		
		for (StateEnum state : StateEnum.values()) {
			List<Legislation> changeHashLegislation = billService.getMasterListChange(state.toString());
			List<Legislation> legislationStubsToFetch = new ArrayList<Legislation>();
			int failures = 0;
			
			log.info("{}: {} entries to check", state.toString(), changeHashLegislation.size());
			
			for (Legislation legislationStub : changeHashLegislation) {
				Optional<LegislationEntity> legislation = legislationRepository.findById(legislationStub.getBill_id());
				if (legislation.isPresent()) {	// We have this bill in our DB already
					
					LegislationEntity legislationEntity = legislation.get();
					if (legislationStub.getChange_hash().equals(legislationEntity.getChange_hash())) {	// Nothing has changed.
						continue;
					}
					
					log.info("We have bill_id {} with changehash {}, but upstream changhash is {}", 
							legislationEntity.getBill_id(), legislationEntity.getChange_hash(), legislationStub.getChange_hash());
					
				} else {	// We don't have a bill in our DB that LegiScan has just reported to us
					
					log.info("We do not have bill_id {} with changehash {}!", 
							legislationStub.getBill_id(), legislationStub.getChange_hash());
					legislationRepository.saveIfDoesNotExist(legislationStub.toEntity()); // TODO: Can be dangerous, leaving only a bill_id and changehash in the db if these calls fall through
					
				}
				
				legislationStubsToFetch.add(legislationStub);
			}
			
			log.info("{}: {} entries to update", state.toString(), legislationStubsToFetch.size());
			
			if (legislationStubsToFetch.size() < 500) {
			
				for (Legislation legislationStub : legislationStubsToFetch) {
					try {
						LegislationEntity fetchedLegislation = billService.getBill(legislationStub).toEntity();
						log.info("\tGot upstream legislation, bill_id {} with changehash {}", 
								fetchedLegislation.getBill_id(), fetchedLegislation.getChange_hash());
						
						legislationRepository.save(fetchedLegislation);
					} catch (Exception e) {
						log.error("Tried to update the following legislation, got error: {}.\n\t{}", e.toString(), legislationStub.toString());
						failures++;
					}
				}
			
			} else {
				log.info("\tToo many entries to update manually, will call for a ZIP file!");
				initializationService.initializeDbFromLegiscanByState(state);
			}

			legislationRepository.flush();
			
			log.info("Done updating legislation, {} updates, {} failures.", legislationStubsToFetch.size() - failures, failures);
		}
		//initializationService.initializeDbFromLegiscanByState(state)
	}
	
	@Scheduled(timeUnit = TimeUnit.DAYS,
			fixedRate = 7,
			initialDelay = 1)
	public void updatePeople() throws UnirestException {
		
		log.info("Updating legislators...");
		
		int added = 0;
		List<RepresentativeEntity> allLegislators = representativeRepository.findAll();
		
		for (RepresentativeEntity representativeDatabase : allLegislators) {
			Representative representativeFresh = representativeService.getRepresentativeById(representativeDatabase.getPeople_id());
			
			if (!representativeFresh.getPerson_hash().equals(representativeDatabase.getPerson_hash())) {
				log.info("We have person_id {} with person_hash {}, but upstream person_hash is {}", 
						representativeDatabase.getPeople_id(), representativeDatabase.getPerson_hash(), representativeFresh.getPerson_hash());
				
				representativeRepository.save(representativeFresh.toEntity());
				added++;
			}
		}
		
		representativeRepository.flush();
		
		log.info("Done updating legislators, {} updates.", added);
		
	}

}

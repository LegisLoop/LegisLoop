package com.backend.legisloop.task;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
	
	@Scheduled(timeUnit = TimeUnit.MINUTES,
			fixedRate = 60,
			initialDelay = 300)
	public void updateLegislation() throws UnirestException, URISyntaxException {
		
		log.info("Updating legislation...");
		
		int toSave = 0;
		
		for (StateEnum state : StateEnum.values()) {
			List<Legislation> changeHashLegislation = billService.getMasterListChange(state.toString());
			log.info("{}: {}", state.toString(), changeHashLegislation.toString());
			
			for (Legislation legislationStub : changeHashLegislation) {
				log.info("\t{}", legislationStub.toString());
				Optional<LegislationEntity> legislation = legislationRepository.findById(legislationStub.getBill_id());
				if (legislation.isPresent()) {	// We have this bill in our DB already
					
					LegislationEntity legislationEntity = legislation.get();
					if (legislationStub.getChange_hash().equals(legislationEntity.getChange_hash())) {	// Nothing has changed.
						continue;
					}
					
					log.info("We have bill_id {} with changehash {}, but upstream changhash is {}", 
							legislationEntity.getBill_id(), legislationEntity.getChange_hash(), legislationStub.getChange_hash());
					LegislationEntity fetchedLegislation = billService.getBill(legislationStub).toEntity();
					
					legislationRepository.save(fetchedLegislation);
					toSave++;
					
				} else {	// We don't have a bill in our DB that LegiScan has just reported to us
					log.info("We do not have bill_id {} with changehash {}!", 
							legislationStub.getBill_id(), legislationStub.getChange_hash());
					legislationRepository.saveIfDoesNotExist(legislationStub.toEntity());
					
					LegislationEntity fetchedLegislation = billService.getBill(legislationStub).toEntity();
					log.info("\tGot upstream legislation, bill_id {} with changehash {}", 
							fetchedLegislation.getBill_id(), fetchedLegislation.getChange_hash());
					
					legislationRepository.save(fetchedLegislation);
					toSave++;
				}
			}
		}
		
		legislationRepository.flush();
		log.info("Done updating legislation, {} updates.", toSave);
	}
	
	@Scheduled(timeUnit = TimeUnit.DAYS,
			fixedRate = 7,
			initialDelay = 7)
	public void updatePeople() throws UnirestException {
		
		log.info("Updating legislators...");
		
		int added = 0;
		List<RepresentativeEntity> allLegislators = representativeRepository.findAll();
		
		for (RepresentativeEntity representativeDatabase : allLegislators) {
			Representative representativeFresh = representativeService.getRepresentativeById(representativeDatabase.getPeople_id());
			
			if (representativeFresh.getPerson_hash() != representativeDatabase.getPerson_hash()) {
				log.debug("We have person_id {} with person_hash {}, but upstream person_hash is {}", 
						representativeDatabase.getPerson_hash(), representativeDatabase.getPerson_hash(), representativeFresh.getPerson_hash());
				
				representativeRepository.save(representativeFresh.toEntity());
				added++;
			}
		}
		
		representativeRepository.flush();
		
		log.info("Done updating legislators, {} updates.", added);
		
	}

}

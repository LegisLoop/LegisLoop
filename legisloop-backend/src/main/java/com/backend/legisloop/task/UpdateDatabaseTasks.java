package com.backend.legisloop.task;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.model.Legislation;
import com.backend.legisloop.model.Representative;
import com.backend.legisloop.repository.LegislationRepository;
import com.backend.legisloop.repository.RepresentativeRepository;
import com.backend.legisloop.service.BillService;
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
    private final BillService billService;
    private final RepresentativeService representativeService;
	
	@Scheduled(timeUnit = TimeUnit.HOURS,
			fixedRate = 1,
			initialDelay = 1)
	public void updateLegislation() throws UnirestException, URISyntaxException {
		
		log.info("Updating legislation...");
		
		List<LegislationEntity> toSave = new ArrayList<LegislationEntity>();
		
		for (StateEnum state : StateEnum.values()) {
			List<Legislation> changeHashLegislation = billService.getMasterListChange(state.toString());
			
			for (Legislation legislationStub : changeHashLegislation) {
				Optional<LegislationEntity> legislation = legislationRepository.findById(legislationStub.getBill_id());
				if (legislation.isPresent()) {	// We have this bill in our DB already
					
					LegislationEntity legislationEntity = legislation.get();
					if (legislationStub.getChange_hash() == legislationEntity.getChange_hash()) {	// Nothing has changed.
						continue;
					}
					
					log.debug("We have bill_id {} with changehash {}, but upstream changhash is {}", 
							legislationEntity.getBill_id(), legislationEntity.getChange_hash(), legislationStub.getChange_hash());
					toSave.add(billService.getBill(legislationStub).toEntity());
					
				} else {	// We don't have a bill in our DB that LegiScan has just reported to us
					log.debug("We do not have bill_id {} with changehash {}!", 
							legislationStub.getBill_id(), legislationStub.getChange_hash());
					toSave.add(billService.getBill(legislationStub).toEntity());
				}
			}
		}
		
		legislationRepository.saveAll(toSave);
		log.info("Done updating legislation, {} updates.", toSave.size());
	}
	
	@Scheduled(timeUnit = TimeUnit.DAYS,
			fixedRate = 7,
			initialDelay = 7)
	public void updatePeople() throws UnirestException {
		
		log.info("Updating legislators...");
		
		int added = 0;
		List<RepresentativeEntity> allLegislators = representativeRepository.findAll();
		
		for (RepresentativeEntity representativeDatabase : allLegislators) {
			Representative representativeFresh = representativeService.update(representativeDatabase.toModel());
			if (representativeFresh.getPerson_hash() != representativeDatabase.getPerson_hash()) {
				added++;
			}
		}
		
		log.info("Done updating legislators, {} updates.", added);
		
	}

}

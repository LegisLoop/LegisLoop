package com.backend.legisloop.repository;

import com.backend.legisloop.entities.LegislationEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegislationRepository extends JpaRepository<LegislationEntity, Integer> {
	
	/**
	 * Save a piece of legislation to the db if it isn't in there already.
	 * Good if trying to add a stub, but not replacing existing content
	 * @param entity
	 * @return
	 */
	default LegislationEntity saveIfDoesNotExist(LegislationEntity entity) {
        Optional<LegislationEntity> existingEntity = findById(entity.getBill_id());
        return existingEntity.orElseGet(() -> save(entity));
    }
}

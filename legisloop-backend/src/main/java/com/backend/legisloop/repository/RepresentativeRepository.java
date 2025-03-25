package com.backend.legisloop.repository;

import com.backend.legisloop.entities.RepresentativeEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentativeRepository extends JpaRepository<RepresentativeEntity, Integer> {
	/**
	 * Save a legislator to the db if it isn't in there already.
	 * Good if trying to add a stub, but not replacing existing content
	 * @param entity
	 * @return
	 */
	default RepresentativeEntity saveIfDoesNotExist(RepresentativeEntity entity) {
        Optional<RepresentativeEntity> existingEntity = findById(entity.getPeople_id());
        return existingEntity.orElseGet(() -> save(entity));
    }
}

package com.backend.legisloop.repository;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepresentativeRepository extends JpaRepository<RepresentativeEntity, Integer> {

    // Find all sponsors of a specific bill
    List<RepresentativeEntity> findBySponsoredBills(LegislationEntity bill);

    @Query("SELECT r FROM RepresentativeEntity r WHERE r.state_id = :stateId")
    List<RepresentativeEntity> findByStateId(int stateId);
    
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

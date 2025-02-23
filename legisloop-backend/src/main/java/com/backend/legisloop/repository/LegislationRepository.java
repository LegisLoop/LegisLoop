package com.backend.legisloop.repository;

import com.backend.legisloop.entities.LegislationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegislationRepository extends JpaRepository<LegislationEntity, Integer> {
}

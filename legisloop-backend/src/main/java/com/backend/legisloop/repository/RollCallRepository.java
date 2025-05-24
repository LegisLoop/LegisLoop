package com.backend.legisloop.repository;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RollCallEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RollCallRepository extends JpaRepository<RollCallEntity, Integer> {

    Page<RollCallEntity> findByLegislation(LegislationEntity legislation, Pageable pageable);
}

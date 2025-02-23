package com.backend.legisloop.repository;

import com.backend.legisloop.entities.RollCallEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RollCallRepository extends JpaRepository<RollCallEntity, Integer> {
}

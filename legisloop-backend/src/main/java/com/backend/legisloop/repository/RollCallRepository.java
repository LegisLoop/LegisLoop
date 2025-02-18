package com.backend.legisloop.repository;

import com.backend.legisloop.entities.RollCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RollCallRepository extends JpaRepository<RollCall, Integer> {
}

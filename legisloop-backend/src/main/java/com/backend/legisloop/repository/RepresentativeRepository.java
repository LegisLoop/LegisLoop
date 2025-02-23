package com.backend.legisloop.repository;

import com.backend.legisloop.entities.RepresentativeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentativeRepository extends JpaRepository<RepresentativeEntity, Integer> {
}

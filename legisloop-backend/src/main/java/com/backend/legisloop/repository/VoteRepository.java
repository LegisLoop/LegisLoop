package com.backend.legisloop.repository;

import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.entities.VoteEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    // Find all votes for a specific representative
    Page<VoteEntity> findByRepresentative(RepresentativeEntity sponsor, Pageable pageable);
}

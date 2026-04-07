package com.backend.legisloop.repository;

import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.entities.VoteEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    // Find all votes for a specific representative
	@Query("SELECT v FROM VoteEntity v JOIN v.rollCall r " +
		       "WHERE v.representative = :sponsor " +
		       "ORDER BY r.date DESC")
    Page<VoteEntity> findByRepresentativeOrderByStatusDateDesc(@Param("sponsor") RepresentativeEntity sponsor, Pageable pageable);
    
    // Find all votes for a specific representative
	@Query("SELECT v FROM VoteEntity v JOIN v.rollCall r " +
		       "WHERE v.representative = :sponsor " +
		       "ORDER BY r.date DESC")
    List<VoteEntity> findByRepresentativeOrderByStatusDateDesc(@Param("sponsor") RepresentativeEntity sponsor);
}

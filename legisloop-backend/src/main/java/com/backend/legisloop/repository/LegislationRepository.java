package com.backend.legisloop.repository;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.enums.StateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegislationRepository extends JpaRepository<LegislationEntity, Integer> {

    Page<LegislationEntity> findByState(StateEnum state, Pageable pageable);

    // Find all legislation where a specific representative is a sponsor
    Page<LegislationEntity> findBySponsors(RepresentativeEntity sponsor, Pageable pageable);

    @Query("SELECT l FROM LegislationEntity l WHERE l.session_id = :sessionId")
    List<LegislationEntity> findBySessionId(@Param("sessionId") int sessionId);

    @Query("SELECT l FROM LegislationEntity l WHERE l.session_id = :sessionId")
    Page<LegislationEntity> findBySessionId(@Param("sessionId") int sessionId, Pageable pageable);
}

package com.backend.legisloop.repository;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
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

    @Query("SELECT r FROM RepresentativeEntity r " +
            "WHERE LOWER(r.first_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.last_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CONCAT(r.first_name, ' ', r.last_name)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<RepresentativeEntity> searchByName(@Param("keyword") String keyword);
}

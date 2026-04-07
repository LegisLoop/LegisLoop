package com.backend.legisloop.repository;

import com.backend.legisloop.entities.LegislationDocumentEntity;
import com.backend.legisloop.entities.LegislationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LegislationDocumentRepository extends JpaRepository<LegislationDocumentEntity, Integer> {
	@Query("SELECT l FROM LegislationDocumentEntity l WHERE l.bill = :bill ORDER BY l.type_id DESC LIMIT 1")
	LegislationDocumentEntity findMostRecentByBill(@Param("bill") LegislationEntity bill);
}

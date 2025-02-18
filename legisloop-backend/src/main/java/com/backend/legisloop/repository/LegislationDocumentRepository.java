package com.backend.legisloop.repository;

import com.backend.legisloop.entities.LegislationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegislationDocumentRepository extends JpaRepository<LegislationDocument, Integer> {
}

package com.backend.legisloop.repository;

import com.backend.legisloop.entities.SummaryEntity;
import com.backend.legisloop.enums.ReadingLevelEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SummaryRepository extends JpaRepository<SummaryEntity, Long> {

    @Query("""
        SELECT s
        FROM SummaryEntity s
        WHERE s.legislationDocument.doc_id = :docId
        AND s.readingLevel = :readingLevel
        """)
    Optional<SummaryEntity> findSummaryByDocIdAndReadingLevel(
            @Param("docId")          int docId,
            @Param("readingLevel")   ReadingLevelEnum readingLevel
    );
}

package com.backend.legisloop.model;

import com.backend.legisloop.entities.LegislationDocumentEntity;
import com.backend.legisloop.entities.SummaryEntity;
import com.backend.legisloop.entities.VoteEntity;
import com.backend.legisloop.enums.ReadingLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Locked;

@AllArgsConstructor
@Builder
@Data
public class Summary {

    private int doc_id;
    private String summaryContent;
    private ReadingLevelEnum readingLevel;

    public SummaryEntity toEntity() {
        return SummaryEntity.builder()
                .legislationDocument(LegislationDocumentEntity.builder().doc_id(this.doc_id).build())
                .summaryContent(this.summaryContent)
                .readingLevel(this.readingLevel)
                .build();
    }
}

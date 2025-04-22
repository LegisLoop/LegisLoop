package com.backend.legisloop.entities;

import com.backend.legisloop.enums.ReadingLevelEnum;
import com.backend.legisloop.model.Summary;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "summaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doc_id", nullable = false)
    private LegislationDocumentEntity legislationDocument;

    @Column(name = "summary_content", columnDefinition = "TEXT")
    private String summaryContent;

    @Column(name = "reading_level")
    private ReadingLevelEnum readingLevel;

    public Summary toModel() {
        return Summary.builder()
                .doc_id(legislationDocument.getDoc_id())
                .summaryContent(this.summaryContent)
                .readingLevel(this.readingLevel)
                .build();
    }
}

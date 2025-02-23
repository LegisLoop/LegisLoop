package com.backend.legisloop.entities;

import com.backend.legisloop.model.LegislationDocument;
import jakarta.persistence.*;
import lombok.*;
import java.net.URI;

@Entity
@Table(name = "legislation_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LegislationDocumentEntity {

    @Id
    private int docId;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private LegislationEntity bill;

    @Column(name = "textHash")
    private String textHash;

    @Column(nullable = true)
    private URI legiscanLink;

    @Column(nullable = true)
    private URI externalLink;

    @Column(name = "mime")
    private String mime;

    @Column(name = "mimeId")
    private int mimeId;

    @Column(columnDefinition = "TEXT")
    private String docContent; // base 64 encoded string

    @Column(name = "type")
    private String type;

    @Column(name = "typeId")
    private int typeId;

    public LegislationDocument toModel() {
        return LegislationDocument.builder()
                .docId(this.docId)
                .billId(this.bill.getBillId())
                .textHash(this.textHash)
                .legiscanLink(this.legiscanLink)
                .externalLink(this.externalLink)
                .mime(this.mime)
                .mimeId(this.mimeId)
                .docContent(this.docContent)
                .type(this.type)
                .typeId(this.typeId)
                .build();
    }
}

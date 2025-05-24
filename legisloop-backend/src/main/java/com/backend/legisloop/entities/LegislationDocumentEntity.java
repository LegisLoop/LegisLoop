package com.backend.legisloop.entities;

import com.backend.legisloop.model.LegislationDocument;
import jakarta.persistence.*;
import lombok.*;
import java.net.URI;
import java.sql.Date;

@Entity
@Table(name = "legislation_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LegislationDocumentEntity {

    @Id
    private int doc_id;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private LegislationEntity bill;

    @Column(name = "date")
    private Date date;

    @Column(name = "textHash")
    private String text_hash;

    @Column(name = "legiscan_link", nullable = true)
    private URI url;

    @Column(name = "external_link", nullable = true)
    private URI state_link;

    @Column(name = "mime")
    private String mime;

    @Column(name = "mimeId")
    private int mimeId;

    @Column(columnDefinition = "TEXT")
    private String docContent; // base 64 encoded string

    @Column(name = "type")
    private String type;

    @Column(name = "type_id")
    private int type_id;

    public LegislationDocument toModel() {
        return LegislationDocument.builder()
                .docId(this.doc_id)
                .billId(this.bill.getBill_id())
                .date(this.date)
                .textHash(this.text_hash)
                .legiscanLink(this.url)
                .externalLink(this.state_link)
                .mime(this.mime)
                .mimeId(this.mimeId)
                .docContent(this.docContent)
                .type(this.type)
                .typeId(this.type_id)
                .build();
    }
}

package com.backend.legisloop.model;

import com.backend.legisloop.entities.LegislationDocumentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.URI;

@AllArgsConstructor
@Builder
@Data
public class LegislationDocument {

    private int docId;
    private int billId;
    private String textHash;
    private URI legiscanLink;
    private URI externalLink;
    private String mime;
    private int mimeId;
    private String docContent;
    private String type;
    private int typeId;

    public LegislationDocumentEntity toEntity() {
        return LegislationDocumentEntity.builder()
                .docId(this.docId)
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

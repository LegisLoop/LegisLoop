package com.backend.legisloop.model;

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
}

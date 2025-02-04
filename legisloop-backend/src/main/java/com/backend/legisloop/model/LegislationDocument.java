package com.backend.legisloop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;

@AllArgsConstructor
@Data
public class LegislationDocument {

    private int docId;
    private int billId;
    private URI legiscanLink;
    private URI externalLink;
    private String mime;
    private String docContent;
}

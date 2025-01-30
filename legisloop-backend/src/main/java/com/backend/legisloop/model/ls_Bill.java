package com.backend.legisloop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@Data
public class ls_Bill {

    private int billId;
    private int sessionId;
    private URI legiscanLink;
    private List<Representative> endorsements;
    private List<ls_LegislationDocument> documents;
}

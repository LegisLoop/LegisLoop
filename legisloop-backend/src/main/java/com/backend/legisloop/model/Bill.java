package com.backend.legisloop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@Data
public class Bill extends Legislation{

    private int billId;
    private int sessionId;
    private URI legiscanLink;
    private List<Representative> endorsements;
    private List<LegislationDocument> documents;
}

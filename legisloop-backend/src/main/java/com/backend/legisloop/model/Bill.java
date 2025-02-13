package com.backend.legisloop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bill extends Legislation{

    private int bill_id;
    private int session_id;
    private URI url; // legiscan link
    private URI stateLink;
    private List<Representative> endorsements;
    public List<LegislationDocument> documents = new ArrayList<>();
}
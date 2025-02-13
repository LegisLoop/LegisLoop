package com.backend.legisloop.model;

import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Legislation {

    private String title;
    private String description;
    private String summary;
    private List<ArticleResponse> newsArticles;
    private List<Representative> sponsors = new ArrayList<>();
    private String change_hash;
    private GoverningBody governingBody;
    private int bill_id;
    private int session_id;
    private URI url; // legiscan link
    private URI stateLink;
    private List<Representative> endorsements;
    public List<LegislationDocument> documents = new ArrayList<>();
}
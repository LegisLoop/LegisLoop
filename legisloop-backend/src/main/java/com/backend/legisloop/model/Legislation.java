package com.backend.legisloop.model;

import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class Legislation {

    private String title;
    private String description;
    private String summary;
    private List<ArticleResponse> newsArticles;
    private List<Representative> sponsors = new ArrayList<>();
    private String change_hash;
    private GoverningBody governingBody;

}

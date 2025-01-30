package com.backend.legisloop.model;

import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Legislation {

    private String title;
    private String description;
    private String summary;
    private List<ArticleResponse> newsArticles;
    private List<Representative> sponsors;
}

package com.backend.legisloop.controller;

import com.backend.legisloop.service.NewsService;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/news")
    public ResponseEntity<ArticleResponse> getArticlesTest(@RequestParam String query,
                                                           @RequestParam String toDate,
                                                           @RequestParam String fromDate,
                                                           @RequestParam int numResults) throws UnirestException {

        return new ResponseEntity<>(newsService.getNewsTest(query, toDate, fromDate, numResults), HttpStatus.OK);
    }
}

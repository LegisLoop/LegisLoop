package com.backend.legisloop.service;

import com.google.gson.Gson;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class NewsService {

    @Value("${newsapi.api.key}")
    private String API_KEY;
    @Value("${newsapi.base.url}")
    private String url;

    public ArticleResponse getNewsTest(String query, String toDate, String fromDate, int numResults) throws UnirestException {

        log.info("Fetching news about {} from {} to {}", query, fromDate, toDate);
        HttpResponse<String> response = Unirest.get(url + "/everything")
                .queryString("q", query)
                .queryString("from", fromDate)
                .queryString("to", toDate)
                .queryString("sortBy", "popularity")
                .queryString("pageSize", numResults)
                .queryString("apiKey", API_KEY)
                .asString();

        if (response.getStatus() == 200) {
            try{
                Gson gson = new Gson();
                return gson.fromJson(response.getBody(), ArticleResponse.class);
            } catch (Exception e){
                log.error(e.getMessage());
                throw e;
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to fetch news, server responded with status: " + response.getStatus());
        }
    }
}

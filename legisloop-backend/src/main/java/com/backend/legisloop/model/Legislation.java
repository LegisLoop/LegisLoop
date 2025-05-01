package com.backend.legisloop.model;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.serial.DateSerializer;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Slf4j
public class Legislation {

    private String title;
    private String description;
    private String summary;
    private List<ArticleResponse> newsArticles;
    private List<Representative> sponsors = new ArrayList<>();
    private String change_hash;
    private GoverningBody governingBody;
    private int bill_id;
    private Integer session_id;
    private URI url; // legiscan link
    private URI stateLink;
    private List<RollCall> roll_calls;
    private List<Representative> endorsements;
    public List<LegislationDocument> documents = new ArrayList<>();
    private StateEnum state;
    private int status;
    private Date status_date;
    private Date dateIntroduced;

    public LegislationEntity toEntity() {
        return LegislationEntity.builder()
                .bill_id(this.bill_id)
                .session_id(this.session_id)
                .title(this.title)
                .description(this.description)
                .summary(this.summary)
                .change_hash(this.change_hash)
                .url(this.url != null ? this.url.toString() : null)
                .state_link(this.url != null ? this.stateLink.toString() : null)
                .texts(this.documents != null ? this.documents.stream().map(LegislationDocument::toEntity).toList() : new ArrayList<>())
                .sponsors(this.sponsors != null ? this.sponsors.stream().map(Representative::toEntity).toList() : new ArrayList<>())
                .endorsements(this.endorsements != null ? this.endorsements.stream().map(Representative::toEntity).toList() : new ArrayList<>())
                .rollCalls(this.roll_calls != null ? this.roll_calls.stream().map(RollCall::toEntity).toList() : new ArrayList<>())
                .state(this.state)
                .status(this.status)
                .status_date(this.status_date)
                .dateIntroduced(this.dateIntroduced)
                .build();
    }

    public static Legislation fillRecord(JsonObject jsonObject) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(java.sql.Date.class, new DateSerializer())
                .create();
        
        Legislation legislation = gson.fromJson(jsonObject, Legislation.class);
        
        // Set state
        int stateId = jsonObject.get("state_id").getAsInt();
        legislation.setState(StateEnum.fromStateID(stateId));
        
        // Set state link
        try {
            legislation.setStateLink(new URI(jsonObject.get("state_link").getAsString()));
        } catch (URISyntaxException e) {
            log.error("Invalid state link URI: {}", e.getMessage());
        }
        
        // Set date introduced from progress array
        JsonArray progress = jsonObject.getAsJsonArray("progress");
        if (progress != null) {
            for (JsonElement element : progress) {
                JsonObject progressObject = element.getAsJsonObject();
                int event = progressObject.get("event").getAsInt();
                if (event == 1) {
                    String dateIntroduced = progressObject.get("date").getAsString();
                    try {
                        legislation.setDateIntroduced(Date.valueOf(LocalDate.parse(dateIntroduced, DateTimeFormatter.ISO_DATE)));
                    } catch (DateTimeParseException e) {
                        log.error("Invalid date format: {}", e.getMessage());
                    }
                    break;
                }
            }
        }
        
        return legislation;
    }
}
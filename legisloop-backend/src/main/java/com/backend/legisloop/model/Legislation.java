package com.backend.legisloop.model;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.enums.StateEnum;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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
                .documents(this.documents != null ? this.documents.stream().map(LegislationDocument::toEntity).toList() : new ArrayList<>())
                .sponsors(this.sponsors != null ? this.sponsors.stream().map(Representative::toEntity).toList() : new ArrayList<>())
                .endorsements(this.endorsements != null ? this.endorsements.stream().map(Representative::toEntity).toList() : new ArrayList<>())
                .rollCalls(this.roll_calls != null ? this.roll_calls.stream().map(RollCall::toEntity).toList() : new ArrayList<>())
                .state(this.state)
                .status(this.status)
                .status_date(this.status_date)
                .dateIntroduced(this.dateIntroduced)
                .build();
    }
}
package com.backend.legisloop.entities;

import com.backend.legisloop.enums.StateEnum;
import com.backend.legisloop.model.Legislation;

import jakarta.persistence.*;
import lombok.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "legislation")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LegislationEntity {

    @Id
    private int bill_id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "summary")
    private String summary;

    @Column(name = "change_hash")
    private String change_hash;

    @Column(name = "url")
    private String url; // LegiScan link

    @Column(name = "state_link")
    private String state_link;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegislationDocumentEntity> documents = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "legislation_sponsors",
            joinColumns = @JoinColumn(name = "bill_id"),
            inverseJoinColumns = @JoinColumn(name = "people_id")
    )
    private List<RepresentativeEntity> sponsors = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "legislation_endorsements",
            joinColumns = @JoinColumn(name = "bill_id"),
            inverseJoinColumns = @JoinColumn(name = "people_id")
    )
    private List<RepresentativeEntity> endorsements = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "legislation_roll_calls",
            joinColumns = @JoinColumn(name = "bill_id"),
            inverseJoinColumns = @JoinColumn(name = "roll_call_id")
    )
    private List<RollCallEntity> rollCalls = new ArrayList<>();

    @Column(name = "state")
    private StateEnum state;

    public Legislation toModel() {
        return Legislation.builder()
                .bill_id(this.bill_id)
                .title(this.title)
                .description(this.description)
                .summary(this.summary)
                .url(URI.create(this.url))
                .stateLink(URI.create(this.state_link))
                .documents(this.documents.stream().map(LegislationDocumentEntity::toModel).toList())
                .sponsors(this.sponsors.stream().map(RepresentativeEntity::toModel).toList())
                .endorsements(this.endorsements.stream().map(RepresentativeEntity::toModel).toList())
                .roll_calls(this.rollCalls.stream().map(RollCallEntity::toModel).toList())
                .state(this.state)
                .change_hash(this.change_hash)
                .build();
    }

}

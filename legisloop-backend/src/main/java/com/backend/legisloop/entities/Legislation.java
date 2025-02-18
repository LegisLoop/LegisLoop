package com.backend.legisloop.entities;

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
public class Legislation {

    @Id
    private int billId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String summary;
    private String changeHash;

    @Column(nullable = true)
    private URI url; // LegiScan link

    @Column(nullable = true)
    private URI stateLink;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegislationDocument> documents = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "legislation_sponsors",
            joinColumns = @JoinColumn(name = "bill_ii"),
            inverseJoinColumns = @JoinColumn(name = "people_id")
    )
    private List<Representative> sponsors = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "legislation_endorsements",
            joinColumns = @JoinColumn(name = "bill_id"),
            inverseJoinColumns = @JoinColumn(name = "people_id")
    )
    private List<Representative> endorsements = new ArrayList<>();

}

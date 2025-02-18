package com.backend.legisloop.entities;

import jakarta.persistence.*;
import lombok.*;
import java.net.URI;

@Entity
@Table(name = "legislation_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LegislationDocument {

    @Id
    private int docId;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private Legislation bill;

    private String textHash;

    @Column(nullable = true)
    private URI legiscanLink;

    @Column(nullable = true)
    private URI externalLink;
    private String mime;
    private int mimeId;

    @Column(columnDefinition = "TEXT")
    private String docContent; // base 64 encoded string

    private String type;
    private int typeId;
}

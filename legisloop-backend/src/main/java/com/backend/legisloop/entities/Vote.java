package com.backend.legisloop.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "votes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private Legislation bill;

    @ManyToOne
    @JoinColumn(name = "roll_call_id", nullable = false)
    private RollCall rollCall;

    @ManyToOne
    @JoinColumn(name = "representative_id", nullable = false)
    private Representative representative;

    private boolean votePosition;
}

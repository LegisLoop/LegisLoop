package com.backend.legisloop.entities;

import com.backend.legisloop.enums.VotePosition;
import com.backend.legisloop.model.Vote;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "votes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private LegislationEntity bill;

    @ManyToOne
    @JoinColumn(name = "roll_call_id", nullable = false)
    private RollCallEntity rollCall;

    @ManyToOne
    @JoinColumn(name = "representative_id", nullable = false)
    private RepresentativeEntity representative;

    @Column(name = "vote_position")
    private VotePosition vote_position;

    public Vote toModel() {
        return Vote.builder()
                .bill_id(this.bill.getBillId())
                .roll_call_id(this.rollCall.getRollCallId())
                .vote_position(this.vote_position)
                .build();
    }

}

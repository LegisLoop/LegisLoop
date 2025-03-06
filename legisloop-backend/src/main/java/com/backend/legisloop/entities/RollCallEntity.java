package com.backend.legisloop.entities;

import com.backend.legisloop.model.RollCall;
import jakarta.persistence.*;
import lombok.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "roll_calls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RollCallEntity {

    @Id
    private int roll_call_id;

    @Column(name = "date")
    private Date date;

    @Column(name = "description")
    private String desc;

    @Column(name = "yea")
    private int yea;

    @Column(name = "nay")
    private int nay;

    @Column(name = "nv")
    private int nv;

    @Column(name = "absent")
    private int absent;

    @Column(name = "total")
    private int total;

    @Column(name = "passed")
    private boolean passed;

    @OneToMany(mappedBy = "rollCall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteEntity> votes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private LegislationEntity legislation;

    public RollCall toModel() {
        return RollCall.builder()
                .roll_call_id(this.roll_call_id)
                .bill_id(this.legislation.getBill_id())
                .date(this.date)
                .yea(this.yea)
                .nay(this.nay)
                .nv(this.nv)
                .absent(this.absent)
                .total(this.total)
                .passed(this.passed)
                .votes(this.votes.stream().map(VoteEntity::toModel).toList())
                .build();
    }

}

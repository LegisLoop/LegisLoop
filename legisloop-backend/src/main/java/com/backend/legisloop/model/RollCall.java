package com.backend.legisloop.model;

import com.backend.legisloop.entities.RollCallEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class RollCall {

	private int roll_call_id;
    private int bill_id;
    private Date date;
    private String desc;
    private int yea;
    private int nay;
    private int nv;
    private int absent;
    private int total;
    private boolean passed;
    private List<Vote> votes;
    private URI url; // legiscan link
    private URI state_link;

    public RollCallEntity toEntity() {
        return RollCallEntity.builder()
                .roll_vall_id(this.roll_call_id)
                .bill_id(this.bill_id)
                .date(this.date)
                .desc(this.desc)
                .yea(this.yea)
                .nay(this.nay)
                .nv(this.nv)
                .absent(this.absent)
                .total(this.total)
                .passed(this.passed)
                .url(this.url)
                .state_link(this.state_link != null ? this.state_link : null)
                .votes(this.votes != null ? this.votes.stream().map(Vote::toEntity).toList() : new ArrayList<>())
                .build();
    }

}

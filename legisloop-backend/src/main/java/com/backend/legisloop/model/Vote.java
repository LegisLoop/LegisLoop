package com.backend.legisloop.model;

import com.backend.legisloop.entities.LegislationEntity;
import com.backend.legisloop.entities.RepresentativeEntity;
import com.backend.legisloop.entities.RollCallEntity;
import com.backend.legisloop.entities.VoteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.URI;

import com.backend.legisloop.enums.VotePosition;

@AllArgsConstructor
@Builder
@Data
public class Vote {

    private int roll_call_id;
    private int person_id;
    private int bill_id; // Not stored in the DB
    private String bill_title; // Not stored in the DB 
    private VotePosition vote_position;

    public VoteEntity toEntity() {
        return VoteEntity.builder()
                .rollCall(RollCallEntity.builder().roll_call_id(this.roll_call_id).build())
                .representative(RepresentativeEntity.builder().people_id(this.person_id).build())
                .vote_position(this.vote_position)
                .build();
    }

}

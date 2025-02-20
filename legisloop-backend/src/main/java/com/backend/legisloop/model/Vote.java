package com.backend.legisloop.model;

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
    private int bill_id;
    private int person_id;
    private VotePosition vote_position;

}

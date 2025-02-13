package com.backend.legisloop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Representative {

    private int people_id;
    private String person_hash;
    private int party_id;
    private int state_id;
    private String party;
    private int role_id;
    private String role;
    private String name;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String district;
    private int ftm_eid;
    private int votesmart_id;
    private int knowwho_pid;
}

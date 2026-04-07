package com.backend.legisloop.model;

import com.backend.legisloop.entities.RepresentativeEntity;
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

    public RepresentativeEntity toEntity() {
        return RepresentativeEntity.builder()
                .people_id(this.people_id)
                .person_hash(this.person_hash)
                .party_id(this.party_id)
                .state_id(this.state_id)
                .party(this.party)
                .role_id(this.role_id)
                .role(this.role)
                .name(this.name)
                .first_name(this.first_name)
                .middle_name(this.middle_name)
                .last_name(this.last_name)
                .district(this.district)
                .ftm_eid(this.ftm_eid)
                .votesmart_id(this.votesmart_id)
                .knowwho_pid(this.knowwho_pid)
                .build();
    }

}

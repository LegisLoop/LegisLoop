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
                .peopleId(this.people_id)
                .personHash(this.person_hash)
                .partyId(this.party_id)
                .stateId(this.state_id)
                .party(this.party)
                .roleId(this.role_id)
                .role(this.role)
                .name(this.name)
                .firstName(this.first_name)
                .middleName(this.middle_name)
                .lastName(this.last_name)
                .district(this.district)
                .ftmEid(this.ftm_eid)
                .votesmartId(this.votesmart_id)
                .knowwhoPid(this.knowwho_pid)
                .build();
    }

}

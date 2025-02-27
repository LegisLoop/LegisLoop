package com.backend.legisloop.entities;

import com.backend.legisloop.model.Representative;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "representatives")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepresentativeEntity {

    @Id
    private int people_id;

    @Column(name = "person_hash")
    private String person_hash;

    @Column(name = "party_id")
    private int party_id;

    @Column(name = "state_id")
    private int state_id;

    @Column(name = "party")
    private String party;

    @Column(name = "role_id")
    private int role_id;

    @Column(name = "role")
    private String role;

    @Column(name = "name")
    private String name;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "middle_name")
    private String middle_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "district")
    private String district;

    @Column(name = "ftm_eid")
    private int ftm_eid;

    @Column(name = "votesmart_id")
    private int votesmart_id;

    @Column(name = "knowwho_pid")
    private int knowwho_pid;

    @ManyToMany(mappedBy = "sponsors")
    private List<LegislationEntity> sponsoredBills = new ArrayList<>();

    @ManyToMany(mappedBy = "endorsements")
    private List<LegislationEntity> endorsedBills = new ArrayList<>();

    public Representative toModel() {
        return Representative.builder()
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

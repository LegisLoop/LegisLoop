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
    private int peopleId;

    @Column(name = "personHash")
    private String personHash;

    @Column(name = "partyId")
    private int partyId;

    @Column(name = "stateId")
    private int stateId;

    @Column(name = "party")
    private String party;

    @Column(name = "roleId")
    private int roleId;

    @Column(name = "role")
    private String role;

    @Column(name = "name")
    private String name;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "district")
    private String district;

    @Column(name = "ftmEid")
    private int ftmEid;

    @Column(name = "votesmartId")
    private int votesmartId;

    @Column(name = "knowwhoPid")
    private int knowwhoPid;

    @ManyToMany(mappedBy = "sponsors")
    private List<LegislationEntity> sponsoredBills = new ArrayList<>();

    @ManyToMany(mappedBy = "endorsements")
    private List<LegislationEntity> endorsedBills = new ArrayList<>();

    public Representative toModel() {
        return Representative.builder()
                .people_id(this.peopleId)
                .person_hash(this.personHash)
                .party_id(this.partyId)
                .state_id(this.stateId)
                .party(this.party)
                .role_id(this.roleId)
                .role(this.role)
                .name(this.name)
                .first_name(this.firstName)
                .middle_name(this.middleName)
                .last_name(this.lastName)
                .district(this.district)
                .ftm_eid(this.ftmEid)
                .votesmart_id(this.votesmartId)
                .knowwho_pid(this.knowwhoPid)
                .build();
    }

}

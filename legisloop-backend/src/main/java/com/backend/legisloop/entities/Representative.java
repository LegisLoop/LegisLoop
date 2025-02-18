package com.backend.legisloop.entities;

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
public class Representative {

    @Id
    private int peopleId;

    private String personHash;
    private int partyId;
    private int stateId;
    private String party;
    private int roleId;
    private String role;
    private String name;
    private String firstName;
    private String middleName;
    private String lastName;
    private String district;
    private int ftmEid;
    private int votesmartId;
    private int knowwhoPid;

    @ManyToMany(mappedBy = "sponsors")
    private List<Legislation> sponsoredBills = new ArrayList<>();

    @ManyToMany(mappedBy = "endorsements")
    private List<Legislation> endorsedBills = new ArrayList<>();
}

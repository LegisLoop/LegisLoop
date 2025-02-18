package com.backend.legisloop.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roll_calls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RollCall {

    @Id
    private int rollCallId;

    @OneToMany(mappedBy = "rollCall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();
}

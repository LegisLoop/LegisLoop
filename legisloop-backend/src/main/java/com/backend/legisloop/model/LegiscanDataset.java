package com.backend.legisloop.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LegiscanDataset {

    private int session_id;
    private int state_id;
    private String access_key;
    private String zip;
}

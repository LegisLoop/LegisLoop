package com.backend.legisloop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class RollCall {

	private int roll_call_id;
    private int bill_id;
    private Date date;
    private String desc;
    private int yea;
    private int nay;
    private int nv;
    private int absent;
    private int total;
    private boolean passed;
    private List<Vote> votes;
    private URI url; // legiscan link
    private URI state_link;

}

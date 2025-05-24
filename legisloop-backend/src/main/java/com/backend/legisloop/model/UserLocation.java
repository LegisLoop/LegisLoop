package com.backend.legisloop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLocation {
    private String name;
    private String road;
    private String neighbourhood;
    private String city;
    private String county;
    private String state;
    private String state_code;
    private String postcode;
    private String country;
    private String country_code;
}

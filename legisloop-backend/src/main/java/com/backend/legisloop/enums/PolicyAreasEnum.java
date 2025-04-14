package com.backend.legisloop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PolicyAreasEnum {
    PUBLIC_HEALTH("Public Health"),
    ECONOMIC_POLICY("Economic Policy"),
    EDUCATION_POLICY("Education Policy"),
    ENVIRONMENTAL_POLICY("environmental policy"),
    RELIGIOUS_POLICY("religious policy"),
    LGBTQ_RIGHTS("LGBTQ rights"),
    CIVIL_RIGHTS("civil rights"),
    PUBLIC_SAFETY("public safety"),
    IMMIGRATION("immigration"),
    DATA_PRIVACY("data privacy"),
    HOUSING_URBAN_POLICY("housing urban policy"),
    INFRASTRUCTURE("infrastructure");

    private final String policyArea;

}

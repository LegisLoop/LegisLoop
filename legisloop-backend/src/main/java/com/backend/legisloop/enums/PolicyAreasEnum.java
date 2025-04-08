package com.backend.legisloop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PolicyAreasEnum {
    PUBLIC_HEALTH("public health"),
    ECONOMIC_POLICY("economic policy"),
    EDUCATION_POLICY("education policy"),
    ENVIRONMENTAL_POLICY("environmental policy"),
    RELIGIOUS_POLICY("religious_policy"),
    LGBTQ_RIGHTS("LGBTQ rights"),
    CIVIL_RIGHTS("civil rights"),
    PUBLIC_SAFETY("public safety"),
    IMMIGRATION("immigration"),
    DATA_PRIVACY("data privacy"),
    HOUSING_URBAN_POLICTY("housing urban policy"),
    INFRASTRUCTURE("infrastructure");

    private final String policyArea;

}

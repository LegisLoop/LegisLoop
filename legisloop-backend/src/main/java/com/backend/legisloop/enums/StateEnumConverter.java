package com.backend.legisloop.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StateEnumConverter implements AttributeConverter<StateEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StateEnum attribute) {
        return attribute == null ? null : attribute.getStateID();
    }

    @Override
    public StateEnum convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : StateEnum.fromStateID(dbData);
    }
}

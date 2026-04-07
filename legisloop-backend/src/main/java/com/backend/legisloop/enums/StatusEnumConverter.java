package com.backend.legisloop.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusEnumConverter implements AttributeConverter<StatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StatusEnum attribute) {
        return attribute == null ? null : attribute.getStatusID();
    }

    @Override
    public StatusEnum convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : StatusEnum.fromStatusID(dbData);
    }
}

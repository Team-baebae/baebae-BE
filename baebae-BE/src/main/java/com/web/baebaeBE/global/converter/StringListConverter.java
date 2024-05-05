package com.web.baebaeBE.global.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {
    @Override
public String convertToDatabaseColumn(List<String> attribute) {
    return (attribute == null || attribute.isEmpty()) ? "" : String.join(",", attribute);
}

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.trim().isEmpty()) ? null : Arrays.asList(dbData.split(","));
    }
}

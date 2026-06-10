package com.Mike.Proj.config;

import java.util.ArrayList;
import java.util.Arrays;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
public class StringListConverter implements AttributeConverter<ArrayList<String>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ArrayList<String> attribute) {
        if (attribute == null) return "[]";
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return new ArrayList<>();
        try {
            return new ArrayList<>(Arrays.asList(mapper.readValue(dbData, String[].class)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

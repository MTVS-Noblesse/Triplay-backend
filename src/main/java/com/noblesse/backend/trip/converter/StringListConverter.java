package com.noblesse.backend.trip.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> entityData){
        try {
            if (entityData == null) {
                return "[]";
            }
            return mapper.writeValueAsString(entityData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
        try {
            if (data == null || data.isEmpty()) {
                return new ArrayList<>();
            }
            return mapper.readValue(data, List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

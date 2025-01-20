package io.example.therapy.therapy.converter;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.example.therapy.therapy.entity.Emotion;

public class EmotionListConverter implements DynamoDBTypeConverter<String, List<Emotion>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<Emotion> object) {
        try {
            return object == null || object.isEmpty() ? "[]" : objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error converting Emotion list to JSON", e);
        }
    }

    @Override
    public List<Emotion> unconvert(String object) {
        try {
            if (object == null || object.isEmpty() || object.equals("[]")) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(object, new TypeReference<List<Emotion>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to Emotion list", e);
        }
    }
}

package io.example.therapy.therapy.converter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class LocalDateTimeListConverter implements DynamoDBTypeConverter<String, List<LocalDateTime>> {

    @Override
    public String convert(List<LocalDateTime> object) {
        return object.stream()
                     .map(LocalDateTime::toString)
                     .collect(Collectors.joining(","));
    }

    @Override
    public List<LocalDateTime> unconvert(String object) {
        return Arrays.stream(object.split(","))
                     .map(LocalDateTime::parse)
                     .collect(Collectors.toList());
    }
}

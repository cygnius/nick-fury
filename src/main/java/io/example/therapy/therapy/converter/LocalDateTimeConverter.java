package io.example.therapy.therapy.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String convert(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER);
    }

    @Override
    public LocalDateTime unconvert(String stringValue) {
        return LocalDateTime.parse(stringValue, FORMATTER);
    }
}

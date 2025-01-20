package io.example.therapy.therapy.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Emotion {

    @NotNull
    private String name;

    @NotNull
    @Min(0) @Max(10)
    private int intensity;

    @NotNull
    private String timestamp; // Change to String (ISO 8601 format)
}

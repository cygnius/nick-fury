package io.example.therapy.therapy.Dtos;

import io.example.therapy.therapy.entity.Emotion;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmotionRequest {
    @NotNull(message = "Emotion must not be null")
    private Emotion emotion;

    @NotNull(message = "Journal ID must not be null")
    private String journalId;

    @NotNull
    @Email
    private String clientEmail;
}

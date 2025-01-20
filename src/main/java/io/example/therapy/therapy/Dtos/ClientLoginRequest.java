package io.example.therapy.therapy.Dtos;

import lombok.Data;

@Data
public class ClientLoginRequest {
    private String email;

    private String password;
}

package io.example.therapy.therapy.controllers;

import lombok.Data;

@Data // Lombok for getters, setters, and constructors
public class LoginRequest {
    private String email;
    private String password;
}

package io.example.therapy.therapy.Dtos;

import java.util.List;

public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private String email;
    private List<String> roles;
  
    public JwtResponse(String accessToken, String refreshToken, String email, List<String> roles) {
      this.accessToken = accessToken;
      this.refreshToken = refreshToken;
      this.email = email;
      this.roles = roles;
    }
  
    public String getAccessToken() {
      return accessToken;
    }
  
    public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
    }
  
    public String getRefreshToken() {
      return refreshToken;
    }
  
    public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
    }
  
    public String getTokenType() {
      return type;
    }
  
    public void setTokenType(String tokenType) {
      this.type = tokenType;
    }
  
    public String getEmail() {
      return email;
    }
  
    public void setEmail(String email) {
      this.email = email;
    }
    public List<String> getRoles() {
      return roles;
    }
  
    public void setRoles(List<String> roles) {
      this.roles = roles;
    }
  }
  
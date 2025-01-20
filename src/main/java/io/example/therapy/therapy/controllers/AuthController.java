package io.example.therapy.therapy.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.example.therapy.therapy.Dtos.JwtResponse;
import io.example.therapy.therapy.JwtTest.security.jwt.JwtUtils;
import io.example.therapy.therapy.JwtTest.security.services.UserDetailsImpl;
import io.example.therapy.therapy.entity.Client;
import io.example.therapy.therapy.entity.Therapist;
import io.example.therapy.therapy.services.Impl.ClientServiceImpl;
import io.example.therapy.therapy.services.Impl.TherapistServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ClientServiceImpl clientService;

    // Save a new client
    @PostMapping("/register")
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        Client savedClient = clientService.saveClient(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    @PostMapping("/login")
public ResponseEntity<?> authenticateUser(@RequestBody io.example.therapy.therapy.Dtos.LoginRequest loginRequest) {

    //System.out.println("Raw password from login request: " + loginRequest.getPassword());
    //System.out.println("Hashed password from login request: " + encoder.encode(loginRequest.getPassword()));
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    //System.out.println("Authentication :"+authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    String refreshToken = jwtUtils.generateRefreshToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, refreshToken, 
                         userDetails.getUsername(), 
                         roles));
}
    @Autowired
    private TherapistServiceImpl therapistService;

    // Endpoint to save a new therapist
    @PostMapping("/registerTH")
    public ResponseEntity<Therapist> saveTherapist(@RequestBody Therapist therapist) {
        Therapist savtherapist = therapistService.saveTherapist(therapist);
        return ResponseEntity.ok(savtherapist);
    }



}

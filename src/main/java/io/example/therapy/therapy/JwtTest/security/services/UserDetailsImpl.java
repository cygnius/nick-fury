package io.example.therapy.therapy.JwtTest.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.example.therapy.therapy.entity.Client;
import io.example.therapy.therapy.entity.Therapist;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl buildFromClient(Client client) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + client.getRole().name()));
        return new UserDetailsImpl(client.getEmail(), client.getPassword(), authorities);
    }

    public static UserDetailsImpl buildFromTherapist(Therapist therapist) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + therapist.getRole()));
        return new UserDetailsImpl(therapist.getEmail(), therapist.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(email, user.email);
    }
}

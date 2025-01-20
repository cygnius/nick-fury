package io.example.therapy.therapy.JwtTest.security.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.example.therapy.therapy.JwtTest.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${app.jwtRefreshExpirationMs}")
  private int jwtRefreshExpirationMs;

  public String generateJwtToken(Authentication authentication) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return generateTokenFromUsername(userPrincipal.getUsername(), jwtExpirationMs);
  }

  public String generateRefreshToken(Authentication authentication) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return generateTokenFromUsername(userPrincipal.getUsername(), jwtRefreshExpirationMs);
  }

  public String generateTokenFromUsername(String username, int expirationMs) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + expirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public int getAccessTokenExpirationMs() {
    return jwtExpirationMs; // or your configured access token expiration time
}

public int getRefreshTokenExpirationMs() {
    return jwtRefreshExpirationMs; // if you have a separate expiration time for refresh tokens
}

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    return validateToken(authToken, false);
  }

  public boolean validateRefreshToken(String refreshToken) {
    return validateToken(refreshToken, true);
  }

  private boolean validateToken(String token, boolean isRefreshToken) {
    try {
      Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
      Date expirationDate = claims.getBody().getExpiration();
      if (isRefreshToken && expirationDate.before(new Date())) {
        logger.error("Refresh token is expired");
        return false;
      }
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}

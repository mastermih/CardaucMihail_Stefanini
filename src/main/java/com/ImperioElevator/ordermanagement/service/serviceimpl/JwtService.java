package com.ImperioElevator.ordermanagement.service.serviceimpl;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // Use configured expiration time
                .signWith(getKey(), SignatureAlgorithm.HS512)  // Sign with the secret key
                .compact();  // Build and return the token
    }

    private Key getKey() {
        // Generate a secure key for HS512 if the provided secretKey is weak
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}

package com.ImperioElevator.ordermanagement.security;

import com.ImperioElevator.ordermanagement.valueobjects.Id;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    public String generateToken(String email, List<String> roles, Boolean account_not_locked, Long id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("id", id);
        claims.put("account_not_locked", account_not_locked);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Long extractUserId(String token){
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token) && extractAccountNonLocked(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean extractAccountNonLocked(String token) {
        return extractClaim(token, claims -> {
            Object claim = claims.get("account_not_locked");
            return claim != null ? (Boolean) claim : true;  // Default to true if claim is missing
        });
    }
// I can use here the extract claim insted of extractAllClaims but I will let it like that for the moment
    public List<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);

        // Extract the roles as a List from the JWT claims
        List<String> roles = claims.get("roles", List.class);

        // Convert  roles to GrantedAuthority
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toUpperCase()))  // ROLE_  may be useless
                .collect(Collectors.toList());
    }
}

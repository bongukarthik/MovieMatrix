package com.movieMatrix.utils;

import com.movieMatrix.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.movieMatrix.models.User;
// For Keys.hmacShaKeyFor()
import io.jsonwebtoken.security.Keys;
// For StandardCharsets.UTF_8
import java.nio.charset.StandardCharsets;
// For the Key class
import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    //    need to remove this and use secretkey instad of newseretkey
//    private String newSecretKey;
    // default 1 hour in milliseconds
    @Value("${jwt.expiration:3600000}")
    private long jwtExpiration;
    //default 3 hours in milliseconds
    @Value("${jwt.refreshTokenExpiration:10800000}")
    private long refreshTokenExpiration;
    Date now = new Date();


    @PostConstruct
    protected void init() {
        // Base64 encode the secret key for better security
        System.out.println("secret Key: " + secretKey);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        System.out.println("secret Key After: " + secretKey);
    }

    public String generateToken(User user) {

        try {
            System.out.println("started generating JWT token for user: {}" + user.getEmail());
            Map<String, Object> claims = createClaims(user);
            System.out.println("jwt Expiration: " + jwtExpiration);
            Date expiryDate = new Date(now.getTime() + jwtExpiration);
            return buildToken(claims, user.getEmail(), expiryDate);
        } catch (Exception e) {
//      log.error("Error generating JWT token for user: {}", user.getEmail(), e);
            System.out.println("Error generating JWT token for user: {}" + user.getEmail() + e);
            throw new JwtAuthenticationException("Failed to generate JWT token");
        }
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("roles", user.getRole());
        // Optional claims
        Optional.ofNullable(user.getProfilePicture())
                .ifPresent(pic -> claims.put("profilePicture", pic));

        return claims;
    }

    private String buildToken(Map<String, Object> claims, String subject, Date expiryDate) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean validateToken(String token) {
        try {
            System.out.println("validate token started");
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
//            log.error("Invalid JWT token: {}", e.getMessage());
            System.out.println("Invalid JWT token: {}" + e.getMessage());
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String generateRefreshToken(User user) {
        try {
            System.out.println("started generating JWT refresh token for user: {}" + user.getEmail());
            Map<String, Object> claims = createClaims(user);
            Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);
            return buildToken(claims, user.getEmail(), expiryDate);
        } catch (Exception e) {
//      log.error("Error generating JWT token for user: {}", user.getEmail(), e);
            System.out.println("Error generating JWT refresh token for user: {}" + user.getEmail() + e);
            throw new JwtAuthenticationException("Failed to generate JWT refresh token");
        }
    }

///End of new implementation almost works


    //remove if not required
//  @SuppressWarnings("deprecation")
//  public static String generateToken(String email, String name) {
//    return Jwts.builder().setSubject(name).setId(email).setIssuedAt(new Date())
//        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 min expiry
//        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
//  }

    // public static String extractUsername(String token) {
    // return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    // }

//    @SuppressWarnings("deprecation")
//    public String generateRefreshToken(String email) {
//        return Jwts.builder().setSubject(email).setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 1)) // 1 day expiry
//                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
//    }

//    public String extractUsername(String token) {
//        return getClaims(token).getSubject();
//    }

    // public String extractUserEmail(String token) {
    // return getClaims(token).getId();
    // }

    // public boolean validateToken(String token) {
    // return extractUsername(token) != null && !isTokenExpired(token);
    // }

//    @SuppressWarnings("deprecation")
//    public Claims getClaims(String token) {
//        System.out.println("token: " + token);
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//    }
//
//    private boolean isTokenExpired(String token) {
//        return getClaims(token).getExpiration().before(new Date());
//    }

//  @SuppressWarnings("deprecation")
//  public static String generateToken(User user) {
//    Map<String, Object> claims = new HashMap<>();
//    claims.put("id", user.getId()); // ✅ Add user ID
//    claims.put("email", user.getEmail()); // ✅ Add email
//    claims.put("name", user.getName()); // ✅ Add name
//    claims.put("roles", user.getRole()); // ✅ Add roles
//    // claims.put("profilePicture", profilePicture); // ✅ Add profile picture URL
//    return Jwts.builder().setClaims(claims).setSubject(user.getEmail()) // Using email as subject
//        .setIssuedAt(new Date())
//        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
//        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
//  }


//    public String extractUserEmail1(String token) {
//        return getClaims(token).get("email", String.class);
//    }


//    public Long extractUserId(String token) {
//        return getClaims(token).get("id", Long.class);
//    }

//    @SuppressWarnings("unchecked")
//    public List<String> extractUserRoles(String token) {
//        return getClaims(token).get("roles", List.class);
//    }

    // public String extractProfilePicture(String token) {
    // return getClaims(token).get("profilePicture", String.class);
    // }

//    public boolean validateToken(String token) {
//        return extractUserEmail(token) != null && !isTokenExpired(token);
//    }

}

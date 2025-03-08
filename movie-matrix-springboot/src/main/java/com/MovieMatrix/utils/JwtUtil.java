package com.MovieMatrix.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.MovieMatrix.models.User;

@Component
public class JwtUtil {
  private static final String SECRET_KEY =
      "ad61b13a839b86a53a558335c21bfd3d7b16133b048c8b494ee230b6395da74cb6ba8c413ec98ce7243385a8b022af043e2138e62de4ced209f7c2b12713fc216ec04a693a48b53dace9df4fce1656dcbe369616de7423cde63b04568494566a5cb9217ca9f9ddd0ae1647e462b01078db657cb6d9e179449c7d52d8ae5ed3df74bc4d659a77e00dbd422b592bc16564416489270fc4ab97af959bd3f1955b3191f472698b19164b293218cb4bdec73244b34cd50c58fbfeffdc024b54a414ff02ec91b688af886c07af5f5b1418b670c4d316a58edca5ec643ff143936c61c5c3a41be7b0bcd233f3a46102e65f939f6b0a8440e80a8581ae2b592480a7f6a7";

  @SuppressWarnings("deprecation")
  public static String generateToken(String email, String name) {
    return Jwts.builder().setSubject(name).setId(email).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 min expiry
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }

  // public static String extractUsername(String token) {
  // return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  // }

  @SuppressWarnings("deprecation")
  public String generateRefreshToken(String email) {
    return Jwts.builder().setSubject(email).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 1)) // 1 day expiry
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
}

  public String extractUsername(String token) {
    return getClaims(token).getSubject();
  }

  // public String extractUserEmail(String token) {
  // return getClaims(token).getId();
  // }

  // public boolean validateToken(String token) {
  // return extractUsername(token) != null && !isTokenExpired(token);
  // }

  @SuppressWarnings("deprecation")
  public Claims getClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  private boolean isTokenExpired(String token) {
    return getClaims(token).getExpiration().before(new Date());
  }

  @SuppressWarnings("deprecation")
  public static String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", user.getId()); // ✅ Add user ID
    claims.put("email", user.getEmail()); // ✅ Add email
    claims.put("name", user.getName()); // ✅ Add name
    claims.put("roles", user.getRole()); // ✅ Add roles
    // claims.put("profilePicture", profilePicture); // ✅ Add profile picture URL
    return Jwts.builder().setClaims(claims).setSubject(user.getEmail()) // Using email as subject
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }


  public String extractUserEmail(String token) {
    return getClaims(token).get("email", String.class);
  }

  public Long extractUserId(String token) {
    return getClaims(token).get("id", Long.class);
  }

  @SuppressWarnings("unchecked")
  public List<String> extractUserRoles(String token) {
    return getClaims(token).get("roles", List.class);
  }

  // public String extractProfilePicture(String token) {
  // return getClaims(token).get("profilePicture", String.class);
  // }

  public boolean validateToken(String token) {
    return extractUserEmail(token) != null && !isTokenExpired(token);
  }


}

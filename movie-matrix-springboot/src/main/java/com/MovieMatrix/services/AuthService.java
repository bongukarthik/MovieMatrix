package com.movieMatrix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.movieMatrix.dtos.RegisterRequest;
import com.movieMatrix.models.Role;
import com.movieMatrix.models.User;
import com.movieMatrix.repositories.UserRepository;
import com.movieMatrix.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtUtil jwtUtil;

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public String register(RegisterRequest request) {
    if (userAlreadyExists(request)) {
      return "user already regestered";
    }
    User user = User.builder().name(request.getName()).email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .phoneNumber(request.getPhoneNumber()).address(request.getAddress())
        .dateOfBirth(request.getDateOfBirth()).role(getRole(request)) // Default
        .profilePicture(request.getProfilePicture()).status(true).registeredAt(LocalDateTime.now())
        .build();
    userRepository.save(user);
    return "User registered successfully";
  }

  private List<Role> getRole(RegisterRequest request) {
    List<Role> roleList = new ArrayList<>();
    if (request.getRole() != null) {
      roleList.addAll(request.getRole());
    }
    return roleList;
  }

  protected boolean userAlreadyExists(RegisterRequest request) {
    boolean userExists =
        userRepository.existsByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber());
    return userExists;
  }

  public String login(RegisterRequest request) {
    Optional<User> user =
        userRepository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber());
    if (user.isPresent()
        && passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
      updateLastLogin(user.get());
      return JwtUtil.generateToken(user.get());
    }
    return "Invalid username or password";
  }

  private void updateLastLogin(User user) {
    user.setLastLoginAt(LocalDateTime.now());
    userRepository.save(user);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
    String refreshToken = request.get("refreshToken");

    try {
      Claims claims = jwtUtil.getClaims(refreshToken);
      // String email = claims.get();
      String id = claims.getId();
      User user = userService.getUser(Long.parseLong(id));
      @SuppressWarnings("static-access")
      String newAccessToken = jwtUtil.generateToken(user);
      return ResponseEntity.ok(Map.of("token", newAccessToken, "refreshToken", refreshToken));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
  }


}

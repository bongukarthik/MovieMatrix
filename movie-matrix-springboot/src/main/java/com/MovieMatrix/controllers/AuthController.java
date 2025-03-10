package com.movieMatrix.controllers;

import com.movieMatrix.dtos.*;
import com.movieMatrix.exceptions.*;
import com.movieMatrix.models.User;
import com.movieMatrix.services.UserService;
import com.movieMatrix.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.movieMatrix.services.AuthService;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    //move this to AuthController
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
//        String refreshToken = request.get("refreshToken");
//
//        try {
//            Claims claims = jwtUtil.extractAllClaims(refreshToken);
//            // String email = claims.get();
//            String id = claims.getId();
//            User user = userService.getUser(Long.parseLong(id));
//            @SuppressWarnings("static-access")
//            String newAccessToken = jwtUtil.generateToken(user);
//            return ResponseEntity.ok(Map.of("token", newAccessToken, "refreshToken", refreshToken));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
//        }
//    }

    //we need to add refresh token on login,
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
//        log.debug("Processing refresh token request");

        try {
            validateRefreshToken(request.getRefreshToken());
            User user = getUserFromToken(request.getRefreshToken());
            TokenResponse tokenResponse = generateNewTokens(user, request.getRefreshToken());

//            log.info("Token refreshed successfully for user: {}", user.getEmail());
            return ResponseEntity.ok(tokenResponse);

        } catch (TokenExpiredException e) {
//            log.warn("Refresh token expired: {}", e.getMessage());
            throw new TokenExpiredException("Refresh token has expired");

        } catch (InvalidTokenException e) {
//            log.warn("Invalid refresh token: {}", e.getMessage());
            throw new InvalidTokenException("Invalid refresh token");

        } catch (Exception e) {
//            log.error("Error processing refresh token: {}", e.getMessage());
            throw new JwtAuthenticationException("Error processing refresh token");
        }
    }

    private void validateRefreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }
    }

    private User getUserFromToken(String refreshToken) {
        Claims claims = jwtUtil.extractAllClaims(refreshToken);
        String userId = claims.getId();

        if (userId == null) {
            throw new InvalidTokenException("User ID not found in token");
        }
        return userService.getUserById(Long.parseLong(userId));
    }

    private TokenResponse generateNewTokens(User user, String refreshToken) {
        String newAccessToken = jwtUtil.generateToken(user);
        return new TokenResponse(newAccessToken, refreshToken);
    }
}
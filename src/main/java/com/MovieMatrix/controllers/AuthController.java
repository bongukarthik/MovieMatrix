package com.MovieMatrix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.MovieMatrix.services.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public Map<String, String> register(@RequestBody Map<String, String> request) {
    String result =
        authService.register(request.get("name"), request.get("email"), request.get("password"));
    Map<String, String> response = new HashMap<>();
    response.put("message", result);
    return response;
  }

  @PostMapping("/login")
  public Map<String, String> login(@RequestBody Map<String, String> request) {
    String token = authService.login(request.get("email"), request.get("password"));
    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    return response;
  }
}

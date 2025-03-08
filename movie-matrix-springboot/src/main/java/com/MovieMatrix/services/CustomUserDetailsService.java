package com.MovieMatrix.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.MovieMatrix.models.User;
import com.MovieMatrix.repositories.UserRepository;

@Service // Ensures Spring recognizes this as a Bean
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
        .password(user.getPassword()) // Ensure password is encrypted
        .authorities("USER") // Set roles/authorities
        .build();
  }
}

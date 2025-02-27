package com.MovieMatrix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.MovieMatrix.filter.JwtFilter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final JwtFilter jwtFilter;
  private final UserDetailsService userDetailsService;

  public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService) {
    this.jwtFilter = jwtFilter;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Provides an AuthenticationManager instance as a Bean to be used in the application context.
   * This is used to authenticate users via a username and password. The user details service is
   * used to load user data from the database, and the password encoder is used to hash passwords
   * before comparing them to the stored password.
   * 
   * @return the configured AuthenticationManager
   */
  @Bean
  public AuthenticationManager authManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(new BCryptPasswordEncoder());
    return new ProviderManager(provider);
  }

  /**
   * Provides a BCryptPasswordEncoder instance as a Bean to be used in the application context. This
   * is used to hash passwords before storing them in the database.
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures the security filter chain to disable CSRF protection and authorize all requests to
   * the "/api/auth/**" endpoint while requiring authentication for all other requests. The JWT
   * filter is added before the username and password authentication filter.
   * 
   * @param http the HttpSecurity object
   * @return the configured SecurityFilterChain
   * @throws Exception if an error occurs while building the security filter chain
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated()) // Protect
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}

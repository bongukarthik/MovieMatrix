package com.movieMatrix.dtos;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String refreshToken;
}


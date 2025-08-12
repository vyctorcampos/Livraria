package com.desafio.livraria.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String type;
    private Instant expiresAt;
}
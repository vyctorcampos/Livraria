package com.desafio.livraria.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.desafio.livraria.dto.request.AuthRequestDTO;
import com.desafio.livraria.dto.request.RegisterRequestDTO;
import com.desafio.livraria.dto.response.LoginResponseDTO;
import com.desafio.livraria.dto.response.RegisterRespondeDTO;
import com.desafio.livraria.model.UserRole;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AuthController")
class AuthControllerTest {

    @InjectMocks
    private AuthController controller;

    @Test
    @DisplayName("Deve criar AuthRequestDTO corretamente")
    void shouldCreateAuthRequestDTO() {
        // Given & When
        AuthRequestDTO requestDTO = new AuthRequestDTO("testuser", "password");

        // Then
        assertThat(requestDTO.login()).isEqualTo("testuser");
        assertThat(requestDTO.password()).isEqualTo("password");
    }

    @Test
    @DisplayName("Deve criar RegisterRequestDTO corretamente")
    void shouldCreateRegisterRequestDTO() {
        // Given & When
        RegisterRequestDTO requestDTO = new RegisterRequestDTO("newuser", "password", UserRole.READ);

        // Then
        assertThat(requestDTO.login()).isEqualTo("newuser");
        assertThat(requestDTO.password()).isEqualTo("password");
        assertThat(requestDTO.role()).isEqualTo(UserRole.READ);
    }

    @Test
    @DisplayName("Deve criar LoginResponseDTO corretamente")
    void shouldCreateLoginResponseDTO() {
        // Given & When
        LoginResponseDTO responseDTO = new LoginResponseDTO("testuser", "jwt-token-here");

        // Then
        assertThat(responseDTO.name()).isEqualTo("testuser");
        assertThat(responseDTO.token()).isEqualTo("jwt-token-here");
    }

    @Test
    @DisplayName("Deve criar RegisterRespondeDTO corretamente")
    void shouldCreateRegisterRespondeDTO() {
        // Given & When
        RegisterRespondeDTO responseDTO = new RegisterRespondeDTO("newuser", "jwt-token-here");

        // Then
        assertThat(responseDTO.login()).isEqualTo("newuser");
        assertThat(responseDTO.token()).isEqualTo("jwt-token-here");
    }
}

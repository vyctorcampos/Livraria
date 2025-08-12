package com.desafio.livraria.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.desafio.livraria.config.security.TokenService;
import com.desafio.livraria.model.UserRole;
import com.desafio.livraria.model.Users;

@DisplayName("Testes do TokenService")
class TokenServiceTest {

    private TokenService tokenService;
    private static final String TEST_SECRET = "test-secret-key-for-unit-tests";

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", TEST_SECRET);
    }

    @Test
    @DisplayName("Deve gerar token JWT válido")
    void generateToken_shouldCreateValidJWT() {
        // Given
        Users user = new Users("testuser", "password", UserRole.READ);

        // When
        String token = tokenService.generateToken(user);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3); // JWT tem 3 partes
    }

    @Test
    @DisplayName("Deve validar token JWT válido")
    void validateToken_shouldReturnSubject_whenTokenIsValid() {
        // Given
        Users user = new Users("testuser", "password", UserRole.READ);
        String token = tokenService.generateToken(user);

        // When
        String subject = tokenService.validateToken(token);

        // Then
        assertThat(subject).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Deve retornar string vazia para token inválido")
    void validateToken_shouldReturnEmptyString_whenTokenIsInvalid() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        String subject = tokenService.validateToken(invalidToken);

        // Then
        assertThat(subject).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar string vazia para token null")
    void validateToken_shouldReturnEmptyString_whenTokenIsNull() {
        // When
        String subject = tokenService.validateToken(null);

        // Then
        assertThat(subject).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar string vazia para token vazio")
    void validateToken_shouldReturnEmptyString_whenTokenIsEmpty() {
        // When
        String subject = tokenService.validateToken("");

        // Then
        assertThat(subject).isEmpty();
    }

    @Test
    @DisplayName("Deve gerar tokens diferentes para usuários diferentes")
    void generateToken_shouldCreateDifferentTokens_forDifferentUsers() {
        // Given
        Users user1 = new Users("user1", "password", UserRole.READ);
        Users user2 = new Users("user2", "password", UserRole.WRITE);

        // When
        String token1 = tokenService.generateToken(user1);
        String token2 = tokenService.generateToken(user2);

        // Then
        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    @DisplayName("Deve gerar tokens diferentes para o mesmo usuário em momentos diferentes")
    void generateToken_shouldCreateDifferentTokens_forSameUserAtDifferentTimes() throws InterruptedException {
        // Given
        Users user = new Users("testuser", "password", UserRole.READ);

        // When
        String token1 = tokenService.generateToken(user);
        Thread.sleep(100); // Pequena pausa para garantir timestamp diferente
        String token2 = tokenService.generateToken(user);

        // Then
        assertThat(token1).isNotEqualTo(token2);
    }
}

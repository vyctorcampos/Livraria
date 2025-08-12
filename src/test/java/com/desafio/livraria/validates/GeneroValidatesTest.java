package com.desafio.livraria.validates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.desafio.livraria.exception.GeneroAlreadyExistsException;
import com.desafio.livraria.exception.GeneroNotFoundException;
import com.desafio.livraria.model.Genero;
import com.desafio.livraria.repository.GeneroRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do GeneroValidates")
class GeneroValidatesTest {

    @Mock
    private GeneroRepository repository;

    private GeneroValidates validates;

    @BeforeEach
    void setUp() {
        validates = new GeneroValidates(repository);
    }

    @Test
    @DisplayName("Deve validar gênero existe por ID")
    void validarGeneroExistePorId_shouldReturnGenero_whenExists() {
        // Given
        UUID id = UUID.randomUUID();
        Genero genero = new Genero();
        genero.setNome("Fantasia");
        when(repository.findById(id)).thenReturn(Optional.of(genero));

        // When
        Genero result = validates.validarGeneroExistePorId(id);

        // Then
        assertThat(result).isSameAs(genero);
    }

    @Test
    @DisplayName("Deve lançar exceção quando gênero não existe por ID")
    void validarGeneroExistePorId_shouldThrowException_whenNotExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> validates.validarGeneroExistePorId(id))
                .isInstanceOf(GeneroNotFoundException.class)
                .hasMessage("Gênero não encontrado com ID: " + id);
    }

    @Test
    @DisplayName("Deve validar gênero existe por nome")
    void validarGeneroExistePorNome_shouldReturnGenero_whenExists() {
        // Given
        String nome = "Fantasia";
        Genero genero = new Genero();
        genero.setNome("Fantasia");
        when(repository.findByNome("Fantasia")).thenReturn(Optional.of(genero));

        // When
        Genero result = validates.validarGeneroExistePorNome(nome);

        // Then
        assertThat(result).isSameAs(genero);
    }

    @Test
    @DisplayName("Deve lançar exceção quando gênero não existe por nome")
    void validarGeneroExistePorNome_shouldThrowException_whenNotExists() {
        // Given
        String nome = "Fantasia";
        when(repository.findByNome("Fantasia")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> validates.validarGeneroExistePorNome(nome))
                .isInstanceOf(GeneroNotFoundException.class)
                .hasMessage("Gênero não encontrado com nome: Fantasia");
    }

    @Test
    @DisplayName("Deve validar nome de gênero não existe")
    void validarNomeGeneroNaoExiste_shouldNotThrowException_whenNameNotExists() {
        // Given
        String nome = "Fantasia";
        when(repository.existsByNome("Fantasia")).thenReturn(false);

        // When & Then
        validates.validarNomeGeneroNaoExiste(nome);
        // Não deve lançar exceção
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome de gênero já existe")
    void validarNomeGeneroNaoExiste_shouldThrowException_whenNameExists() {
        // Given
        String nome = "Fantasia";
        when(repository.existsByNome("Fantasia")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> validates.validarNomeGeneroNaoExiste(nome))
                .isInstanceOf(GeneroAlreadyExistsException.class)
                .hasMessage("Já existe um gênero com o nome: Fantasia");
    }

    @Test
    @DisplayName("Deve validar nome de gênero não existe para atualização")
    void validarNomeGeneroNaoExisteParaAtualizacao_shouldNotThrowException_whenNameNotExists() {
        // Given
        String nome = "Fantasia";
        UUID id = UUID.randomUUID();
        when(repository.existsByNomeAndIdNot("Fantasia", id)).thenReturn(false);

        // When & Then
        validates.validarNomeGeneroNaoExisteParaAtualizacao(nome, id);
        // Não deve lançar exceção
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome de gênero já existe para atualização")
    void validarNomeGeneroNaoExisteParaAtualizacao_shouldThrowException_whenNameExists() {
        // Given
        String nome = "Fantasia";
        UUID id = UUID.randomUUID();
        when(repository.existsByNomeAndIdNot("Fantasia", id)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> validates.validarNomeGeneroNaoExisteParaAtualizacao(nome, id))
                .isInstanceOf(GeneroAlreadyExistsException.class)
                .hasMessage("Já existe outro gênero com o nome: Fantasia");
    }

    @Test
    @DisplayName("Deve validar gênero existe para remoção")
    void validarGeneroExisteParaRemocao_shouldNotThrowException_whenExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        // When & Then
        validates.validarGeneroExisteParaRemocao(id);
        // Não deve lançar exceção
    }

    @Test
    @DisplayName("Deve lançar exceção quando gênero não existe para remoção")
    void validarGeneroExisteParaRemocao_shouldThrowException_whenNotExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> validates.validarGeneroExisteParaRemocao(id))
                .isInstanceOf(GeneroNotFoundException.class)
                .hasMessage("Gênero não encontrado com ID: " + id);
    }

    @Test
    @DisplayName("Deve normalizar nome capitalizando primeira letra")
    void normalizarNome_shouldCapitalizeFirstLetter() {
        // Given
        String nome = "  fANTASIA  ";

        // When
        String result = validates.normalizarNome(nome);

        // Then
        assertThat(result).isEqualTo("Fantasia");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for vazio")
    void normalizarNome_shouldThrowException_whenNameIsEmpty() {
        // Given
        String nome = "   ";

        // When & Then
        assertThatThrownBy(() -> validates.normalizarNome(nome))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for null")
    void normalizarNome_shouldThrowException_whenNameIsNull() {
        // When & Then
        assertThatThrownBy(() -> validates.normalizarNome(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome não pode ser vazio");
    }
}

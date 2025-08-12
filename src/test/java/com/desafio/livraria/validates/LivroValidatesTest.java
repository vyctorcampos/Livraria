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

import com.desafio.livraria.model.Livro;
import com.desafio.livraria.repository.LivroRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do LivroValidates")
class LivroValidatesTest {

    @Mock
    private LivroRepository repository;

    private LivroValidates validates;

    @BeforeEach
    void setUp() {
        validates = new LivroValidates(repository);
    }

    @Test
    @DisplayName("Deve validar livro existe por ID")
    void validarLivroExistePorId_shouldReturnLivro_whenExists() {
        // Given
        UUID id = UUID.randomUUID();
        Livro livro = new Livro();
        livro.setTitulo("O Senhor dos Anéis");
        when(repository.findById(id)).thenReturn(Optional.of(livro));

        // When
        Livro result = validates.validarLivroExistePorId(id);

        // Then
        assertThat(result).isSameAs(livro);
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro não existe por ID")
    void validarLivroExistePorId_shouldThrowException_whenNotExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> validates.validarLivroExistePorId(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livro não encontrado com o ID informado.");
    }

    @Test
    @DisplayName("Deve validar título de livro não existe")
    void validarTituloLivroNaoExiste_shouldNotThrowException_whenTitleNotExists() {
        // Given
        String titulo = "O Senhor dos Anéis";
        when(repository.existsByTitulo(titulo.trim())).thenReturn(false);

        // When & Then
        validates.validarTituloLivroNaoExiste(titulo);
        // Não deve lançar exceção
    }

    @Test
    @DisplayName("Deve lançar exceção quando título de livro já existe")
    void validarTituloLivroNaoExiste_shouldThrowException_whenTitleExists() {
        // Given
        String titulo = "O Senhor dos Anéis";
        when(repository.existsByTitulo(titulo.trim())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> validates.validarTituloLivroNaoExiste(titulo))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Já existe um livro cadastrado com esse título.");
    }

    @Test
    @DisplayName("Deve validar título de livro não existe para atualização")
    void validarTituloLivroNaoExisteParaAtualizacao_shouldNotThrowException_whenTitleNotExists() {
        // Given
        String titulo = "O Senhor dos Anéis";
        UUID id = UUID.randomUUID();
        when(repository.existsByTituloAndIdNot(titulo.trim(), id)).thenReturn(false);

        // When & Then
        validates.validarTituloLivroNaoExisteParaAtualizacao(titulo, id);
        // Não deve lançar exceção
    }

    @Test
    @DisplayName("Deve lançar exceção quando título de livro já existe para atualização")
    void validarTituloLivroNaoExisteParaAtualizacao_shouldThrowException_whenTitleExists() {
        // Given
        String titulo = "O Senhor dos Anéis";
        UUID id = UUID.randomUUID();
        when(repository.existsByTituloAndIdNot(titulo.trim(), id)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> validates.validarTituloLivroNaoExisteParaAtualizacao(titulo, id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Já existe outro livro cadastrado com esse título.");
    }

    @Test
    @DisplayName("Deve validar livro existe por título")
    void validarLivroExistePorTitulo_shouldReturnLivro_whenExists() {
        // Given
        String titulo = "O Senhor dos Anéis";
        Livro livro = new Livro();
        livro.setTitulo("O Senhor dos Anéis");
        when(repository.findByTitulo(titulo.trim())).thenReturn(Optional.of(livro));

        // When
        Livro result = validates.validarLivroExistePorTitulo(titulo);

        // Then
        assertThat(result).isSameAs(livro);
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro não existe por título")
    void validarLivroExistePorTitulo_shouldThrowException_whenNotExists() {
        // Given
        String titulo = "O Senhor dos Anéis";
        when(repository.findByTitulo(titulo.trim())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> validates.validarLivroExistePorTitulo(titulo))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livro não encontrado com o título informado.");
    }

    @Test
    @DisplayName("Deve validar livro existe para remoção")
    void validarLivroExisteParaRemocao_shouldNotThrowException_whenExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        // When & Then
        validates.validarLivroExisteParaRemocao(id);
        // Não deve lançar exceção
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro não existe para remoção")
    void validarLivroExisteParaRemocao_shouldThrowException_whenNotExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> validates.validarLivroExisteParaRemocao(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livro não encontrado para remoção.");
    }

    @Test
    @DisplayName("Deve normalizar título removendo espaços")
    void normalizarTitulo_shouldTrimSpaces() {
        // Given
        String titulo = "  O Senhor dos Anéis  ";

        // When
        String result = validates.normalizarTitulo(titulo);

        // Then
        assertThat(result).isEqualTo("O Senhor dos Anéis");
    }
}

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

import com.desafio.livraria.model.Autor;
import com.desafio.livraria.repository.AutorRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AutorValidates")
class AutorValidatesTest {

    @Mock
    private AutorRepository repository;

    private AutorValidates validates;

    @BeforeEach
    void setUp() {
        validates = new AutorValidates(repository);
    }

    @Test
    @DisplayName("Deve validar autor existe por ID")
    void validarAutorExistePorId_shouldReturnAutor_whenExists() {
        // Given
        UUID id = UUID.randomUUID();
        Autor autor = new Autor();
        autor.setNome("João Silva");
        when(repository.findById(id)).thenReturn(Optional.of(autor));

        // When
        Autor result = validates.validarAutorExistePorId(id);

        // Then
        assertThat(result).isSameAs(autor);
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não existe por ID")
    void validarAutorExistePorId_shouldThrowException_whenNotExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> validates.validarAutorExistePorId(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Autor não encontrado com o ID informado.");
    }

    @Test
    @DisplayName("Deve validar nome de autor não existe")
    void validarNomeAutorNaoExiste_shouldNotThrowException_whenNameNotExists() {
        // Given
        String nome = "João Silva";
        when(repository.existsByNome(nome.trim())).thenReturn(false);

        // When & Then
        validates.validarNomeAutorNaoExiste(nome);
        // Não deve lançar exceção
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome de autor já existe")
    void validarNomeAutorNaoExiste_shouldThrowException_whenNameExists() {
        // Given
        String nome = "João Silva";
        when(repository.existsByNome(nome.trim())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> validates.validarNomeAutorNaoExiste(nome))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Já existe um autor cadastrado com esse nome.");
    }

    @Test
    @DisplayName("Deve validar nome de autor não existe para atualização")
    void validarNomeAutorNaoExisteParaAtualizacao_shouldNotThrowException_whenNameNotExists() {
        // Given
        String nome = "João Silva";
        UUID id = UUID.randomUUID();
        when(repository.existsByNomeAndIdNot(nome.trim(), id)).thenReturn(false);

        // When & Then
        validates.validarNomeAutorNaoExisteParaAtualizacao(nome, id);
        // Não deve lançar exceção
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome de autor já existe para atualização")
    void validarNomeAutorNaoExisteParaAtualizacao_shouldThrowException_whenNameExists() {
        // Given
        String nome = "João Silva";
        UUID id = UUID.randomUUID();
        when(repository.existsByNomeAndIdNot(nome.trim(), id)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> validates.validarNomeAutorNaoExisteParaAtualizacao(nome, id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Já existe outro autor cadastrado com esse nome.");
    }

    @Test
    @DisplayName("Deve validar autor existe por nome")
    void validarAutorExistePorNome_shouldReturnAutor_whenExists() {
        // Given
        String nome = "João Silva";
        Autor autor = new Autor();
        autor.setNome("João Silva");
        when(repository.findByNome(nome.trim())).thenReturn(Optional.of(autor));

        // When
        Autor result = validates.validarAutorExistePorNome(nome);

        // Then
        assertThat(result).isSameAs(autor);
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não existe por nome")
    void validarAutorExistePorNome_shouldThrowException_whenNotExists() {
        // Given
        String nome = "João Silva";
        when(repository.findByNome(nome.trim())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> validates.validarAutorExistePorNome(nome))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Autor não encontrado com o nome informado.");
    }

    @Test
    @DisplayName("Deve validar autor existe para remoção")
    void validarAutorExisteParaRemocao_shouldNotThrowException_whenExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        // When & Then
        validates.validarAutorExisteParaRemocao(id);
        // Não deve lançar exceção
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não existe para remoção")
    void validarAutorExisteParaRemocao_shouldThrowException_whenNotExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> validates.validarAutorExisteParaRemocao(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Autor não encontrado para remoção.");
    }

    @Test
    @DisplayName("Deve normalizar nome removendo espaços")
    void normalizarNome_shouldTrimSpaces() {
        // Given
        String nome = "  João Silva  ";

        // When
        String result = validates.normalizarNome(nome);

        // Then
        assertThat(result).isEqualTo("João Silva");
    }
}

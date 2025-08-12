package com.desafio.livraria.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.desafio.livraria.dto.request.LivroRequestDTO;
import com.desafio.livraria.dto.response.LivroResponseDTO;
import com.desafio.livraria.model.Autor;
import com.desafio.livraria.model.Genero;
import com.desafio.livraria.model.Livro;

@DisplayName("Testes do LivroMapper")
class LivroMapperTest {

    private AutorMapper autorMapper;
    private GeneroMapper generoMapper;
    private LivroMapper mapper;

    @BeforeEach
    void setUp() {
        autorMapper = new AutorMapper();
        generoMapper = new GeneroMapper();
        mapper = new LivroMapper(autorMapper, generoMapper);
    }

    @Test
    @DisplayName("Deve converter LivroRequestDTO para Livro entity")
    void toEntity_shouldMapFields() {
        // Given
        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("O Senhor dos Anéis");
        dto.setAutorId(UUID.randomUUID());
        dto.setGeneroId(UUID.randomUUID());

        Autor autor = new Autor();
        autor.setNome("J.R.R. Tolkien");
        Genero genero = new Genero();
        genero.setNome("Fantasia");

        // When
        Livro livro = mapper.toEntity(dto, autor, genero);

        // Then
        assertNotNull(livro);
        assertThat(livro.getTitulo()).isEqualTo("O Senhor dos Anéis");
        assertThat(livro.getAutor()).isSameAs(autor);
        assertThat(livro.getGenero()).isSameAs(genero);
    }

    @Test
    @DisplayName("Deve converter Livro entity para LivroResponseDTO")
    void toResponseDTO_shouldMapFields() {
        // Given
        Autor autor = new Autor();
        autor.setNome("J.R.R. Tolkien");
        autor.setEmail("tolkien@example.com");
        autor.setIdade(81);

        Genero genero = new Genero();
        genero.setNome("Fantasia");

        Livro livro = new Livro();
        livro.setTitulo("O Hobbit");
        livro.setAutor(autor);
        livro.setGenero(genero);

        // When
        LivroResponseDTO dto = mapper.toResponseDTO(livro);

        // Then
        assertNotNull(dto);
        assertThat(dto.getTitulo()).isEqualTo("O Hobbit");
        assertThat(dto.getAutor().getNome()).isEqualTo("J.R.R. Tolkien");
        assertThat(dto.getGenero().getNome()).isEqualTo("Fantasia");
    }

    @Test
    @DisplayName("Deve retornar null quando DTO for null")
    void toEntity_shouldReturnNull_whenDtoIsNull() {
        // When
        Livro livro = mapper.toEntity(null, new Autor(), new Genero());

        // Then
        assertThat(livro).isNull();
    }

    @Test
    @DisplayName("Deve retornar null quando entity for null")
    void toResponseDTO_shouldReturnNull_whenEntityIsNull() {
        // When
        LivroResponseDTO dto = mapper.toResponseDTO(null);

        // Then
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Deve atualizar entidade existente com dados do DTO")
    void updateEntityFromDTO_shouldUpdateFields() {
        // Given
        Livro livro = new Livro();
        livro.setTitulo("Título Antigo");

        Autor autor = new Autor();
        autor.setNome("Autor Novo");
        Genero genero = new Genero();
        genero.setNome("Gênero Novo");

        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("Título Novo");

        // When
        mapper.updateEntityFromDTO(dto, livro, autor, genero);

        // Then
        assertThat(livro.getTitulo()).isEqualTo("Título Novo");
        assertThat(livro.getAutor()).isSameAs(autor);
        assertThat(livro.getGenero()).isSameAs(genero);
    }

    @Test
    @DisplayName("Deve criar entidade sem relacionamentos")
    void toEntityWithoutRelations_shouldCreateEntity() {
        // Given
        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("Livro Teste");

        // When
        Livro livro = mapper.toEntityWithoutRelations(dto);

        // Then
        assertNotNull(livro);
        assertThat(livro.getTitulo()).isEqualTo("Livro Teste");
        assertThat(livro.getAutor()).isNull();
        assertThat(livro.getGenero()).isNull();
    }
}

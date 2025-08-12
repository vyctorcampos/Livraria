package com.desafio.livraria.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.desafio.livraria.dto.request.AutorRequestDTO;
import com.desafio.livraria.dto.response.AutorResponseDTO;
import com.desafio.livraria.model.Autor;

@DisplayName("Testes do AutorMapper")
class AutorMapperTest {

    private AutorMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AutorMapper();
    }

    @Test
    @DisplayName("Deve converter AutorRequestDTO para Autor entity")
    void toEntity_shouldMapFields() {
        // Given
        AutorRequestDTO dto = new AutorRequestDTO();
        dto.setNome("João Silva");
        dto.setEmail("joao@example.com");
        dto.setIdade(30);

        // When
        Autor autor = mapper.toEntity(dto);

        // Then
        assertNotNull(autor);
        assertThat(autor.getNome()).isEqualTo("João Silva");
        assertThat(autor.getEmail()).isEqualTo("joao@example.com");
        assertThat(autor.getIdade()).isEqualTo(30);
    }

    @Test
    @DisplayName("Deve converter Autor entity para AutorResponseDTO")
    void toResponseDTO_shouldMapFields() {
        // Given
        Autor autor = new Autor();
        autor.setNome("Maria Santos");
        autor.setEmail("maria@example.com");
        autor.setIdade(25);

        // When
        AutorResponseDTO dto = mapper.toResponseDTO(autor);

        // Then
        assertNotNull(dto);
        assertThat(dto.getNome()).isEqualTo("Maria Santos");
        assertThat(dto.getEmail()).isEqualTo("maria@example.com");
        assertThat(dto.getIdade()).isEqualTo(25);
    }

    @Test
    @DisplayName("Deve retornar null quando DTO for null")
    void toEntity_shouldReturnNull_whenDtoIsNull() {
        // When
        Autor autor = mapper.toEntity(null);

        // Then
        assertThat(autor).isNull();
    }

    @Test
    @DisplayName("Deve retornar null quando entity for null")
    void toResponseDTO_shouldReturnNull_whenEntityIsNull() {
        // When
        AutorResponseDTO dto = mapper.toResponseDTO(null);

        // Then
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Deve atualizar entidade existente com dados do DTO")
    void updateEntityFromDTO_shouldUpdateFields() {
        // Given
        Autor autor = new Autor();
        autor.setNome("Nome Antigo");
        autor.setEmail("email@antigo.com");
        autor.setIdade(40);

        AutorRequestDTO dto = new AutorRequestDTO();
        dto.setNome("Nome Novo");
        dto.setEmail("email@novo.com");
        dto.setIdade(35);

        // When
        mapper.updateEntityFromDTO(dto, autor);

        // Then
        assertThat(autor.getNome()).isEqualTo("Nome Novo");
        assertThat(autor.getEmail()).isEqualTo("email@novo.com");
        assertThat(autor.getIdade()).isEqualTo(35);
    }
}

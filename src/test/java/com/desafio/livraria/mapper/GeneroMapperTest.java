package com.desafio.livraria.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.desafio.livraria.dto.request.GeneroRequestDTO;
import com.desafio.livraria.dto.response.GeneroResponseDTO;
import com.desafio.livraria.model.Genero;

@DisplayName("Testes do GeneroMapper")
class GeneroMapperTest {

    private GeneroMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new GeneroMapper();
    }

    @Test
    @DisplayName("Deve converter GeneroRequestDTO para Genero entity")
    void toEntity_shouldMapFields() {
        // Given
        GeneroRequestDTO dto = new GeneroRequestDTO();
        dto.setNome("Fantasia");

        // When
        Genero genero = mapper.toEntity(dto);

        // Then
        assertNotNull(genero);
        assertThat(genero.getNome()).isEqualTo("Fantasia");
    }

    @Test
    @DisplayName("Deve converter Genero entity para GeneroResponseDTO")
    void toResponseDTO_shouldMapFields() {
        // Given
        Genero genero = new Genero();
        genero.setNome("Terror");

        // When
        GeneroResponseDTO dto = mapper.toResponseDTO(genero);

        // Then
        assertNotNull(dto);
        assertThat(dto.getNome()).isEqualTo("Terror");
    }

    @Test
    @DisplayName("Deve retornar null quando DTO for null")
    void toEntity_shouldReturnNull_whenDtoIsNull() {
        // When
        Genero genero = mapper.toEntity(null);

        // Then
        assertThat(genero).isNull();
    }

    @Test
    @DisplayName("Deve retornar null quando entity for null")
    void toResponseDTO_shouldReturnNull_whenEntityIsNull() {
        // When
        GeneroResponseDTO dto = mapper.toResponseDTO(null);

        // Then
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Deve atualizar entidade existente com dados do DTO")
    void updateEntityFromDTO_shouldUpdateFields() {
        // Given
        Genero genero = new Genero();
        genero.setNome("Nome Antigo");

        GeneroRequestDTO dto = new GeneroRequestDTO();
        dto.setNome("Nome Novo");

        // When
        mapper.updateEntityFromDTO(dto, genero);

        // Then
        assertThat(genero.getNome()).isEqualTo("Nome Novo");
    }
}

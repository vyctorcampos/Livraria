package com.desafio.livraria.mapper;

import org.springframework.stereotype.Component;

import com.desafio.livraria.dto.request.GeneroRequestDTO;
import com.desafio.livraria.dto.response.GeneroResponseDTO;
import com.desafio.livraria.model.Genero;

/**
 * Mapper para conversões entre Genero entity e DTOs
 */
@Component
public class GeneroMapper {

    /**
     * Converte GeneroRequestDTO para Genero entity
     */
    public Genero toEntity(GeneroRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Genero genero = new Genero();
        genero.setNome(dto.getNome());
        return genero;
    }

    /**
     * Converte Genero entity para GeneroResponseDTO
     */
    public GeneroResponseDTO toResponseDTO(Genero genero) {
        if (genero == null) {
            return null;
        }
        
        GeneroResponseDTO dto = new GeneroResponseDTO();
        dto.setId(genero.getId());
        dto.setNome(genero.getNome());
        return dto;
    }

    /**
     * Atualiza uma entidade Genero existente com dados do DTO
     */
    public void updateEntityFromDTO(GeneroRequestDTO dto, Genero genero) {
        if (dto != null && genero != null) {
            genero.setNome(dto.getNome());
        }
    }
}

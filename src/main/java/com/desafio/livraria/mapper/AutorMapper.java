package com.desafio.livraria.mapper;

import org.springframework.stereotype.Component;

import com.desafio.livraria.dto.request.AutorRequestDTO;
import com.desafio.livraria.dto.response.AutorResponseDTO;
import com.desafio.livraria.model.Autor;

@Component
public class AutorMapper {

    public Autor toEntity(AutorRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Autor autor = new Autor();
        autor.setNome(dto.getNome());
        autor.setEmail(dto.getEmail());
        autor.setIdade(dto.getIdade());
        return autor;
    }

    public AutorResponseDTO toResponseDTO(Autor autor) {
        if (autor == null) {
            return null;
        }
        
        AutorResponseDTO dto = new AutorResponseDTO();
        dto.setId(autor.getId());
        dto.setNome(autor.getNome());
        dto.setEmail(autor.getEmail());
        dto.setIdade(autor.getIdade());
        return dto;
    }

    public void updateEntityFromDTO(AutorRequestDTO dto, Autor autor) {
        if (dto != null && autor != null) {
            autor.setNome(dto.getNome());
            autor.setEmail(dto.getEmail());
            autor.setIdade(dto.getIdade());
        }
    }
}

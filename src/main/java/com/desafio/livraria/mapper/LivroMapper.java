package com.desafio.livraria.mapper;

import org.springframework.stereotype.Component;

import com.desafio.livraria.dto.request.LivroRequestDTO;
import com.desafio.livraria.dto.response.LivroResponseDTO;
import com.desafio.livraria.model.Autor;
import com.desafio.livraria.model.Genero;
import com.desafio.livraria.model.Livro;

/**
 * Mapper para conversões entre Livro entity e DTOs
 */
@Component
public class LivroMapper {

    private final AutorMapper autorMapper;
    private final GeneroMapper generoMapper;

    public LivroMapper(AutorMapper autorMapper, GeneroMapper generoMapper) {
        this.autorMapper = autorMapper;
        this.generoMapper = generoMapper;
    }

    /**
     * Converte LivroRequestDTO para Livro entity
     * Nota: Autor e Gênero precisam ser buscados separadamente pelo ID
     */
    public Livro toEntity(LivroRequestDTO livroRequestDTO, Autor autor, Genero genero) {
        if (livroRequestDTO == null) {
            return null;
        }
        
        Livro livro = new Livro();
        livro.setTitulo(livroRequestDTO.getTitulo());
        livro.setAutor(autor);
        livro.setGenero(genero);
        return livro;
    }

    /**
     * Converte Livro entity para LivroResponseDTO
     */
    public LivroResponseDTO toResponseDTO(Livro livro) {
        if (livro == null) {
            return null;
        }
        
        LivroResponseDTO dto = new LivroResponseDTO();
        dto.setId(livro.getId());
        dto.setTitulo(livro.getTitulo());
        dto.setAutor(autorMapper.toResponseDTO(livro.getAutor()));
        dto.setGenero(generoMapper.toResponseDTO(livro.getGenero()));
        return dto;
    }

    /**
     * Atualiza uma entidade Livro existente com dados do DTO
     */
    public void updateEntityFromDTO(LivroRequestDTO dto, Livro livro, Autor autor, Genero genero) {
        if (dto != null && livro != null) {
            livro.setTitulo(dto.getTitulo());
            if (autor != null) {
                livro.setAutor(autor);
            }
            if (genero != null) {
                livro.setGenero(genero);
            }
        }
    }

    /**
     * Método auxiliar para criar entidade sem relacionamentos (útil para validações)
     */
    public Livro toEntityWithoutRelations(LivroRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        return livro;
    }
}

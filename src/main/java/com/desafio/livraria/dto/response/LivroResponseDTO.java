package com.desafio.livraria.dto.response;

import java.util.UUID;

import lombok.Data;

@Data
public class LivroResponseDTO {

    private UUID id;
    private String titulo;
    private AutorResponseDTO autor;
    private GeneroResponseDTO genero;

    
}

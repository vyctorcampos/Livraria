package com.desafio.livraria.dto.response;

import java.util.UUID;

import lombok.Data;

@Data
public class AutorResponseDTO {

    private UUID id;
    private String nome;
    private String email;
    private Integer idade;

    
}

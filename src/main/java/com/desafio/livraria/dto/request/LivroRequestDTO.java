package com.desafio.livraria.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LivroRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 2, max = 255, message = "Título deve ter entre 2 e 255 caracteres")
    private String titulo;

    @NotNull(message = "ID do autor é obrigatório")
    private UUID autorId;

    @NotNull(message = "ID do gênero é obrigatório")
    private UUID generoId;

    
}

package com.desafio.livraria.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AutorRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 255, message = "Nome deve ter entre 2 e 255 caracteres")
    private String nome;

    @Email(message = "Email deve ser válido")
    private String email;

    @Min(value = 1, message = "Idade deve ser maior que 0")
    private Integer idade;

}

package com.desafio.livraria.service;

import java.util.List;
import java.util.UUID;

import com.desafio.livraria.dto.request.GeneroRequestDTO;
import com.desafio.livraria.dto.response.GeneroResponseDTO;

public interface GeneroService  {
    

    GeneroResponseDTO criarGenero(GeneroRequestDTO generoRequestDTO);
    
    GeneroResponseDTO buscarPorId(UUID id);
    
    List<GeneroResponseDTO> listarTodos();
    
    GeneroResponseDTO atualizarGenero(UUID id, GeneroRequestDTO generoRequestDTO);
    
    void removerGenero(UUID id);

    GeneroResponseDTO buscarPorNome(String nome);
}

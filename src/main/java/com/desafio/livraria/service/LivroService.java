package com.desafio.livraria.service;

import java.util.List;
import java.util.UUID;

import com.desafio.livraria.dto.request.LivroRequestDTO;
import com.desafio.livraria.dto.response.LivroResponseDTO;

public interface LivroService  {
    

	LivroResponseDTO criarLivro(LivroRequestDTO livroRequestDTO);

	LivroResponseDTO buscarPorId(UUID id);

	List<LivroResponseDTO> listarTodos();

	LivroResponseDTO atualizarLivro(UUID id, LivroRequestDTO livroRequestDTO);

	void removerLivro(UUID id);

	LivroResponseDTO buscarPorTitulo(String titulo);
}

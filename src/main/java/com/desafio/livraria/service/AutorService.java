package com.desafio.livraria.service;

import java.util.List;
import java.util.UUID;

import com.desafio.livraria.dto.request.AutorRequestDTO;
import com.desafio.livraria.dto.response.AutorResponseDTO;


public interface AutorService {
   	

	AutorResponseDTO criarAutor(AutorRequestDTO autorRequestDTO);

	List<AutorResponseDTO> listarTodos();

	AutorResponseDTO buscarPorId(UUID id);

	AutorResponseDTO atualizarAutor(UUID id, AutorRequestDTO autorRequestDTO);

	void removerAutor(UUID id);

	AutorResponseDTO buscarPorNome(String nome);
}

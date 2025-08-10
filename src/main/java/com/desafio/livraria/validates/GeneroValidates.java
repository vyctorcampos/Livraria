package com.desafio.livraria.validates;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.desafio.livraria.exception.GeneroAlreadyExistsException;
import com.desafio.livraria.exception.GeneroNotFoundException;
import com.desafio.livraria.model.Genero;
import com.desafio.livraria.repository.GeneroRepository;

/**
 * Classe responsável por todas as validações relacionadas ao Genero
 */
@Component
public class GeneroValidates {
    
    private final GeneroRepository generoRepository;
    
    public GeneroValidates(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    public Genero validarGeneroExistePorId(UUID id) {
        return generoRepository.findById(id)
            .orElseThrow(() -> new GeneroNotFoundException("Gênero não encontrado com ID: " + id));
    }

    public Genero validarGeneroExistePorNome(String nome) {
        String nomeNormalizado = normalizarNome(nome);
        return generoRepository.findByNome(nomeNormalizado)
            .orElseThrow(() -> new GeneroNotFoundException("Gênero não encontrado com nome: " + nomeNormalizado));
    }

    public void validarNomeGeneroNaoExiste(String nome) {
        String nomeNormalizado = normalizarNome(nome);
        if (generoRepository.existsByNome(nomeNormalizado)) {
            throw new GeneroAlreadyExistsException("Já existe um gênero com o nome: " + nomeNormalizado);
        }
    }
    

    public void validarNomeGeneroNaoExisteParaAtualizacao(String nome, UUID id) {
        String nomeNormalizado = normalizarNome(nome);
        if (generoRepository.existsByNomeAndIdNot(nomeNormalizado, id)) {
            throw new GeneroAlreadyExistsException("Já existe outro gênero com o nome: " + nomeNormalizado);
        }
    }
    

    public void validarGeneroExisteParaRemocao(UUID id) {
        if (!generoRepository.existsById(id)) {
            throw new GeneroNotFoundException("Gênero não encontrado com ID: " + id);
        }
    }
    

    public String normalizarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        
        String nomeNormalizado = nome.trim();
        return nomeNormalizado.substring(0, 1).toUpperCase() + 
               nomeNormalizado.substring(1).toLowerCase();
    }
}

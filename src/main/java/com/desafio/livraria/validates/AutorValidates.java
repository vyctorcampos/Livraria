package com.desafio.livraria.validates;

import com.desafio.livraria.model.Autor;
import com.desafio.livraria.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AutorValidates {

    private final AutorRepository autorRepository;

    public AutorValidates(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public Autor validarAutorExistePorId(UUID autorId) {
        return autorRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com o ID informado."));
    }

    public void validarNomeAutorNaoExiste(String nome) {
        if (autorRepository.existsByNome(nome.trim())) {
            throw new RuntimeException("Já existe um autor cadastrado com esse nome.");
        }
    }

    public void validarNomeAutorNaoExisteParaAtualizacao(String nome, UUID id) {
        if (autorRepository.existsByNomeAndIdNot(nome.trim(), id)) {
            throw new RuntimeException("Já existe outro autor cadastrado com esse nome.");
        }
    }

    public Autor validarAutorExistePorNome(String nome) {
        return autorRepository.findByNome(nome.trim())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com o nome informado."));
    }

    public void validarAutorExisteParaRemocao(UUID id) {
        if (!autorRepository.existsById(id)) {
            throw new RuntimeException("Autor não encontrado para remoção.");
        }
    }

    public String normalizarNome(String nome) {
        return nome.trim();
    }
}
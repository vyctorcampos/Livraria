package com.desafio.livraria.validates;

import com.desafio.livraria.model.Livro;
import com.desafio.livraria.repository.LivroRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LivroValidates {

    private final LivroRepository livroRepository;

    public LivroValidates(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro validarLivroExistePorId(UUID livroId) {
        return livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o ID informado."));
    }

    public void validarTituloLivroNaoExiste(String titulo) {
        if (livroRepository.existsByTitulo(titulo.trim())) {
            throw new RuntimeException("Já existe um livro cadastrado com esse título.");
        }
    }

    public void validarTituloLivroNaoExisteParaAtualizacao(String titulo, UUID id) {
        if (livroRepository.existsByTituloAndIdNot(titulo.trim(), id)) {
            throw new RuntimeException("Já existe outro livro cadastrado com esse título.");
        }
    }

    public Livro validarLivroExistePorTitulo(String titulo) {
        return livroRepository.findByTitulo(titulo.trim())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o título informado."));
    }

    public void validarLivroExisteParaRemocao(UUID id) {
        if (!livroRepository.existsById(id)) {
            throw new RuntimeException("Livro não encontrado para remoção.");
        }
    }

    public String normalizarTitulo(String titulo) {
        return titulo.trim();
    }
}

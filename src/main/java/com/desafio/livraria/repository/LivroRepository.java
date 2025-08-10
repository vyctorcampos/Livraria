package com.desafio.livraria.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio.livraria.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID> {

    Optional<Livro> findByTitulo(String titulo);

    boolean existsByTitulo(String titulo);

    boolean existsByTituloAndIdNot(String titulo, UUID id);
}


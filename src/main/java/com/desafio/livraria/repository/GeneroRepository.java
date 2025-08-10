package com.desafio.livraria.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio.livraria.model.Genero;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, UUID> {
    
    Optional<Genero> findByNome(String nome);

    boolean existsByNome(String nome);
    
    boolean existsByNomeAndIdNot(String nome, UUID id);
}

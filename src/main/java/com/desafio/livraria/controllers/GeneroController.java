package com.desafio.livraria.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.livraria.dto.request.GeneroRequestDTO;
import com.desafio.livraria.dto.response.GeneroResponseDTO;
import com.desafio.livraria.exception.GeneroAlreadyExistsException;
import com.desafio.livraria.exception.GeneroNotFoundException;
import com.desafio.livraria.service.GeneroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/generos")
@Validated
public class GeneroController {
    
    @Autowired
    private GeneroService generoService;
    

    @PostMapping
    public ResponseEntity<GeneroResponseDTO> criarGenero(@Valid @RequestBody GeneroRequestDTO generoRequestDTO) {
        try {
            GeneroResponseDTO generoCreated = generoService.criarGenero(generoRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(generoCreated);
        } catch (GeneroAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> buscarPorId(@PathVariable UUID id) {
        try {
            GeneroResponseDTO genero = generoService.buscarPorId(id);
            return ResponseEntity.ok(genero);
        } catch (GeneroNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<GeneroResponseDTO>> listarTodos() {
        List<GeneroResponseDTO> generos = generoService.listarTodos();
        return ResponseEntity.ok(generos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<GeneroResponseDTO> buscarPorNome(@RequestParam String nome) {
        try {
            GeneroResponseDTO genero = generoService.buscarPorNome(nome);
            return ResponseEntity.ok(genero);
        } catch (GeneroNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> atualizarGenero(
            @PathVariable UUID id, 
            @Valid @RequestBody GeneroRequestDTO generoRequestDTO) {
        try {
            GeneroResponseDTO generoAtualizado = generoService.atualizarGenero(id, generoRequestDTO);
            return ResponseEntity.ok(generoAtualizado);
        } catch (GeneroNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (GeneroAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerGenero(@PathVariable UUID id) {
        try {
            generoService.removerGenero(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (GeneroNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

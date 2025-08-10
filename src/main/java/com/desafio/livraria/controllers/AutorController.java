package com.desafio.livraria.controllers;

import com.desafio.livraria.dto.request.AutorRequestDTO;
import com.desafio.livraria.dto.response.AutorResponseDTO;
import com.desafio.livraria.service.AutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<AutorResponseDTO> criarAutor(@RequestBody AutorRequestDTO autorRequestDTO) {
        AutorResponseDTO autorCriado = autorService.criarAutor(autorRequestDTO);
        return new ResponseEntity<>(autorCriado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarPorId(@PathVariable UUID id) {
        AutorResponseDTO autor = autorService.buscarPorId(id);
        return ResponseEntity.ok(autor);
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listarTodos() {
        List<AutorResponseDTO> autores = autorService.listarTodos();
        return ResponseEntity.ok(autores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizarAutor(@PathVariable UUID id, @RequestBody AutorRequestDTO autorRequestDTO) {
        AutorResponseDTO autorAtualizado = autorService.atualizarAutor(id, autorRequestDTO);
        return ResponseEntity.ok(autorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerAutor(@PathVariable UUID id) {
        autorService.removerAutor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-por-nome")
    public ResponseEntity<AutorResponseDTO> buscarPorNome(@RequestParam String nome) {
        AutorResponseDTO autor = autorService.buscarPorNome(nome);
        return ResponseEntity.ok(autor);
    }
}


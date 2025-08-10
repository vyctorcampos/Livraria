package com.desafio.livraria.controllers;

import com.desafio.livraria.dto.request.LivroRequestDTO;
import com.desafio.livraria.dto.response.LivroResponseDTO;
import com.desafio.livraria.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/livros")
@Validated
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroResponseDTO> criarLivro(@Valid @RequestBody LivroRequestDTO livroRequestDTO) {
        LivroResponseDTO livroCriado = livroService.criarLivro(livroRequestDTO);
        return new ResponseEntity<>(livroCriado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable UUID id) {
        LivroResponseDTO livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarTodos() {
        List<LivroResponseDTO> livros = livroService.listarTodos();
        return ResponseEntity.ok(livros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizarLivro(@PathVariable UUID id, @Valid @RequestBody LivroRequestDTO livroRequestDTO) {
        LivroResponseDTO livroAtualizado = livroService.atualizarLivro(id, livroRequestDTO);
        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerLivro(@PathVariable UUID id) {
        livroService.removerLivro(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-por-titulo")
    public ResponseEntity<LivroResponseDTO> buscarPorTitulo(@RequestParam String titulo) {
        LivroResponseDTO livro = livroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(livro);
    }
}

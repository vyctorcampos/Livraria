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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/livros")
@Validated
@Tag(name = "Livros", description = "CRUD de livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    @Operation(summary = "Criar livro", security = {@SecurityRequirement(name = "bearer-jwt")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Criado",
                            content = @Content(schema = @Schema(implementation = LivroResponseDTO.class)))
            })
    public ResponseEntity<LivroResponseDTO> criarLivro(@Valid @RequestBody LivroRequestDTO livroRequestDTO) {
        LivroResponseDTO livroCriado = livroService.criarLivro(livroRequestDTO);
        return new ResponseEntity<>(livroCriado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable UUID id) {
        LivroResponseDTO livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @GetMapping
    @Operation(summary = "Listar livros", security = {@SecurityRequirement(name = "bearer-jwt")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LivroResponseDTO.class))))
            })
    public ResponseEntity<List<LivroResponseDTO>> listarTodos() {
        List<LivroResponseDTO> livros = livroService.listarTodos();
        return ResponseEntity.ok(livros);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<LivroResponseDTO> atualizarLivro(@PathVariable UUID id, @Valid @RequestBody LivroRequestDTO livroRequestDTO) {
        LivroResponseDTO livroAtualizado = livroService.atualizarLivro(id, livroRequestDTO);
        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover livro", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<Void> removerLivro(@PathVariable UUID id) {
        livroService.removerLivro(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-por-titulo")
    @Operation(summary = "Buscar livro por título", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<LivroResponseDTO> buscarPorTitulo(@RequestParam String titulo) {
        LivroResponseDTO livro = livroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(livro);
    }
}

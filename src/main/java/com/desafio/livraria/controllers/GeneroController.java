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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/generos")
@Tag(name = "Gêneros", description = "CRUD de gêneros literários")
@Validated
public class GeneroController {
    
    @Autowired
    private GeneroService generoService;
    

    @PostMapping
    @Operation(summary = "Criar gênero", security = {@SecurityRequirement(name = "bearer-jwt")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Criado",
                            content = @Content(schema = @Schema(implementation = GeneroResponseDTO.class))),
                    @ApiResponse(responseCode = "409", description = "Conflito"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida")
            })
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
    @Operation(summary = "Buscar gênero por ID", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<GeneroResponseDTO> buscarPorId(@PathVariable UUID id) {
        try {
            GeneroResponseDTO genero = generoService.buscarPorId(id);
            return ResponseEntity.ok(genero);
        } catch (GeneroNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Listar gêneros", security = {@SecurityRequirement(name = "bearer-jwt")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GeneroResponseDTO.class))))
            })
    public ResponseEntity<List<GeneroResponseDTO>> listarTodos() {
        List<GeneroResponseDTO> generos = generoService.listarTodos();
        return ResponseEntity.ok(generos);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar gênero por nome", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<GeneroResponseDTO> buscarPorNome(@RequestParam String nome) {
        try {
            GeneroResponseDTO genero = generoService.buscarPorNome(nome);
            return ResponseEntity.ok(genero);
        } catch (GeneroNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar gênero", security = {@SecurityRequirement(name = "bearer-jwt")})
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
    @Operation(summary = "Remover gênero", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<Void> removerGenero(@PathVariable UUID id) {
        try {
            generoService.removerGenero(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (GeneroNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

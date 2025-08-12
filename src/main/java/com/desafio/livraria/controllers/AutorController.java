package com.desafio.livraria.controllers;

import com.desafio.livraria.dto.request.AutorRequestDTO;
import com.desafio.livraria.dto.response.AutorResponseDTO;
import com.desafio.livraria.service.AutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
@Tag(name = "Autores", description = "CRUD de autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    @Operation(summary = "Criar autor", security = {@SecurityRequirement(name = "bearer-jwt")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Criado",
                            content = @Content(schema = @Schema(implementation = AutorResponseDTO.class)))
            })
    public ResponseEntity<AutorResponseDTO> criarAutor(@RequestBody AutorRequestDTO autorRequestDTO) {
        AutorResponseDTO autorCriado = autorService.criarAutor(autorRequestDTO);
        return new ResponseEntity<>(autorCriado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID", security = {@SecurityRequirement(name = "bearer-jwt")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AutorResponseDTO.class)))
            })
    public ResponseEntity<AutorResponseDTO> buscarPorId(@PathVariable UUID id) {
        AutorResponseDTO autor = autorService.buscarPorId(id);
        return ResponseEntity.ok(autor);
    }

    @GetMapping
    @Operation(summary = "Listar autores", security = {@SecurityRequirement(name = "bearer-jwt")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AutorResponseDTO.class))))
            })
    public ResponseEntity<List<AutorResponseDTO>> listarTodos() {
        List<AutorResponseDTO> autores = autorService.listarTodos();
        return ResponseEntity.ok(autores);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar autor", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<AutorResponseDTO> atualizarAutor(@PathVariable UUID id, @RequestBody AutorRequestDTO autorRequestDTO) {
        AutorResponseDTO autorAtualizado = autorService.atualizarAutor(id, autorRequestDTO);
        return ResponseEntity.ok(autorAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover autor", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<Void> removerAutor(@PathVariable UUID id) {
        autorService.removerAutor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar autor por nome", security = {@SecurityRequirement(name = "bearer-jwt")})
    public ResponseEntity<AutorResponseDTO> buscarPorNome(@RequestParam  String nome) {
        try {
        	AutorResponseDTO autor = autorService.buscarPorNome(nome);
        	 return ResponseEntity.ok(autor);
        }catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
    	
       
    }
}


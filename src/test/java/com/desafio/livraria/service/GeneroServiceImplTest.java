package com.desafio.livraria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.desafio.livraria.dto.request.GeneroRequestDTO;
import com.desafio.livraria.dto.response.GeneroResponseDTO;
import com.desafio.livraria.mapper.GeneroMapper;
import com.desafio.livraria.model.Genero;
import com.desafio.livraria.repository.GeneroRepository;
import com.desafio.livraria.service.impl.GeneroServiceImpl;
import com.desafio.livraria.validates.GeneroValidates;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do GeneroServiceImpl")
class GeneroServiceImplTest {

    @Mock
    private GeneroRepository repository;

    @Mock
    private GeneroMapper mapper;

    @Mock
    private GeneroValidates validates;

    private GeneroService service;

    @BeforeEach
    void setUp() {
        service = new GeneroServiceImpl(repository, mapper, validates);
    }

    @Test
    @DisplayName("Deve criar gênero com sucesso")
    void criarGenero_shouldCreateGeneroSuccessfully() {
        // Given
        GeneroRequestDTO requestDTO = new GeneroRequestDTO();
        requestDTO.setNome("Fantasia");

        Genero genero = new Genero();
        genero.setNome("Fantasia");

        GeneroResponseDTO responseDTO = new GeneroResponseDTO();
        responseDTO.setNome("Fantasia");

        when(repository.save(any(Genero.class))).thenReturn(genero);
        when(mapper.toResponseDTO(genero)).thenReturn(responseDTO);

        // When
        GeneroResponseDTO result = service.criarGenero(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Fantasia");
        verify(validates).validarNomeGeneroNaoExiste("Fantasia");
        verify(repository).save(any(Genero.class));
    }

    @Test
    @DisplayName("Deve buscar gênero por ID com sucesso")
    void buscarPorId_shouldReturnGenero_whenExists() {
        // Given
        UUID id = UUID.randomUUID();
        Genero genero = new Genero();
        genero.setNome("Fantasia");

        GeneroResponseDTO responseDTO = new GeneroResponseDTO();
        responseDTO.setNome("Fantasia");

        when(validates.validarGeneroExistePorId(id)).thenReturn(genero);
        when(mapper.toResponseDTO(genero)).thenReturn(responseDTO);

        // When
        GeneroResponseDTO result = service.buscarPorId(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Fantasia");
        verify(validates).validarGeneroExistePorId(id);
    }

    @Test
    @DisplayName("Deve listar todos os gêneros")
    void listarTodos_shouldReturnAllGeneros() {
        // Given
        Genero genero1 = new Genero();
        genero1.setNome("Fantasia");
        Genero genero2 = new Genero();
        genero2.setNome("Terror");

        GeneroResponseDTO response1 = new GeneroResponseDTO();
        response1.setNome("Fantasia");
        GeneroResponseDTO response2 = new GeneroResponseDTO();
        response2.setNome("Terror");

        when(repository.findAll()).thenReturn(List.of(genero1, genero2));
        when(mapper.toResponseDTO(genero1)).thenReturn(response1);
        when(mapper.toResponseDTO(genero2)).thenReturn(response2);

        // When
        List<GeneroResponseDTO> result = service.listarTodos();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNome()).isEqualTo("Fantasia");
        assertThat(result.get(1).getNome()).isEqualTo("Terror");
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve atualizar gênero com sucesso")
    void atualizarGenero_shouldUpdateGeneroSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();
        GeneroRequestDTO requestDTO = new GeneroRequestDTO();
        requestDTO.setNome("Fantasia Atualizado");

        Genero generoExistente = new Genero();
        generoExistente.setNome("Fantasia");

        GeneroResponseDTO responseDTO = new GeneroResponseDTO();
        responseDTO.setNome("Fantasia Atualizado");

        when(validates.validarGeneroExistePorId(id)).thenReturn(generoExistente);
        when(repository.save(any(Genero.class))).thenReturn(generoExistente);
        when(mapper.toResponseDTO(generoExistente)).thenReturn(responseDTO);

        // When
        GeneroResponseDTO result = service.atualizarGenero(id, requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Fantasia Atualizado");
        verify(validates).validarGeneroExistePorId(id);
        verify(validates).validarNomeGeneroNaoExisteParaAtualizacao("Fantasia Atualizado", id);
        verify(repository).save(generoExistente);
    }

    @Test
    @DisplayName("Deve remover gênero com sucesso")
    void removerGenero_shouldRemoveGeneroSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        service.removerGenero(id);

        // Then
        verify(validates).validarGeneroExisteParaRemocao(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Deve buscar gênero por nome com sucesso")
    void buscarPorNome_shouldReturnGenero_whenExists() {
        // Given
        String nome = "Fantasia";
        Genero genero = new Genero();
        genero.setNome("Fantasia");

        GeneroResponseDTO responseDTO = new GeneroResponseDTO();
        responseDTO.setNome("Fantasia");

        when(validates.validarGeneroExistePorNome(nome)).thenReturn(genero);
        when(mapper.toResponseDTO(genero)).thenReturn(responseDTO);

        // When
        GeneroResponseDTO result = service.buscarPorNome(nome);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Fantasia");
        verify(validates).validarGeneroExistePorNome(nome);
    }
}

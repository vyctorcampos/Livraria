package com.desafio.livraria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import com.desafio.livraria.dto.request.AutorRequestDTO;
import com.desafio.livraria.dto.response.AutorResponseDTO;
import com.desafio.livraria.mapper.AutorMapper;
import com.desafio.livraria.model.Autor;
import com.desafio.livraria.repository.AutorRepository;
import com.desafio.livraria.service.impl.AutorServiceImpl;
import com.desafio.livraria.validates.AutorValidates;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AutorServiceImpl")
class AutorServiceImplTest {

    @Mock
    private AutorRepository repository;

    @Mock
    private AutorMapper mapper;

    @Mock
    private AutorValidates validates;

    private AutorService service;

    @BeforeEach
    void setUp() {
        service = new AutorServiceImpl(repository, mapper, validates);
    }

    @Test
    @DisplayName("Deve criar autor com sucesso")
    void criarAutor_shouldCreateAutorSuccessfully() {
        // Given
        AutorRequestDTO requestDTO = new AutorRequestDTO();
        requestDTO.setNome("João Silva");
        requestDTO.setEmail("joao@example.com");
        requestDTO.setIdade(30);

        Autor autor = new Autor();
        autor.setNome("João Silva");
        autor.setEmail("joao@example.com");
        autor.setIdade(30);

        AutorResponseDTO responseDTO = new AutorResponseDTO();
        responseDTO.setNome("João Silva");
        responseDTO.setEmail("joao@example.com");
        responseDTO.setIdade(30);

        when(repository.save(any(Autor.class))).thenReturn(autor);
        when(mapper.toResponseDTO(autor)).thenReturn(responseDTO);

        // When
        AutorResponseDTO result = service.criarAutor(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("João Silva");
        verify(validates).validarNomeAutorNaoExiste("João Silva");
        verify(repository).save(any(Autor.class));
    }

    @Test
    @DisplayName("Deve buscar autor por ID com sucesso")
    void buscarPorId_shouldReturnAutor_whenExists() {
        // Given
        UUID id = UUID.randomUUID();
        Autor autor = new Autor();
        autor.setNome("João Silva");

        AutorResponseDTO responseDTO = new AutorResponseDTO();
        responseDTO.setNome("João Silva");

        when(validates.validarAutorExistePorId(id)).thenReturn(autor);
        when(mapper.toResponseDTO(autor)).thenReturn(responseDTO);

        // When
        AutorResponseDTO result = service.buscarPorId(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("João Silva");
        verify(validates).validarAutorExistePorId(id);
    }

    @Test
    @DisplayName("Deve listar todos os autores")
    void listarTodos_shouldReturnAllAutores() {
        // Given
        Autor autor1 = new Autor();
        autor1.setNome("João Silva");
        Autor autor2 = new Autor();
        autor2.setNome("Maria Santos");

        AutorResponseDTO response1 = new AutorResponseDTO();
        response1.setNome("João Silva");
        AutorResponseDTO response2 = new AutorResponseDTO();
        response2.setNome("Maria Santos");

        when(repository.findAll()).thenReturn(List.of(autor1, autor2));
        when(mapper.toResponseDTO(autor1)).thenReturn(response1);
        when(mapper.toResponseDTO(autor2)).thenReturn(response2);

        // When
        List<AutorResponseDTO> result = service.listarTodos();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNome()).isEqualTo("João Silva");
        assertThat(result.get(1).getNome()).isEqualTo("Maria Santos");
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve atualizar autor com sucesso")
    void atualizarAutor_shouldUpdateAutorSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();
        AutorRequestDTO requestDTO = new AutorRequestDTO();
        requestDTO.setNome("João Silva Atualizado");

        Autor autorExistente = new Autor();
        autorExistente.setNome("João Silva");

        AutorResponseDTO responseDTO = new AutorResponseDTO();
        responseDTO.setNome("João Silva Atualizado");

        when(validates.validarAutorExistePorId(id)).thenReturn(autorExistente);
        when(repository.save(any(Autor.class))).thenReturn(autorExistente);
        when(mapper.toResponseDTO(autorExistente)).thenReturn(responseDTO);

        // When
        AutorResponseDTO result = service.atualizarAutor(id, requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("João Silva Atualizado");
        verify(validates).validarAutorExistePorId(id);
        verify(validates).validarNomeAutorNaoExisteParaAtualizacao("João Silva Atualizado", id);
        verify(repository).save(autorExistente);
    }

    @Test
    @DisplayName("Deve remover autor com sucesso")
    void removerAutor_shouldRemoveAutorSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        service.removerAutor(id);

        // Then
        verify(validates).validarAutorExisteParaRemocao(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Deve buscar autor por nome com sucesso")
    void buscarPorNome_shouldReturnAutor_whenExists() {
        // Given
        String nome = "João Silva";
        Autor autor = new Autor();
        autor.setNome("João Silva");

        AutorResponseDTO responseDTO = new AutorResponseDTO();
        responseDTO.setNome("João Silva");

        when(validates.validarAutorExistePorNome(nome)).thenReturn(autor);
        when(mapper.toResponseDTO(autor)).thenReturn(responseDTO);

        // When
        AutorResponseDTO result = service.buscarPorNome(nome);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("João Silva");
        verify(validates).validarAutorExistePorNome(nome);
    }
}

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

import com.desafio.livraria.dto.request.LivroRequestDTO;
import com.desafio.livraria.dto.response.LivroResponseDTO;
import com.desafio.livraria.mapper.AutorMapper;
import com.desafio.livraria.mapper.GeneroMapper;
import com.desafio.livraria.mapper.LivroMapper;
import com.desafio.livraria.model.Autor;
import com.desafio.livraria.model.Genero;
import com.desafio.livraria.model.Livro;
import com.desafio.livraria.repository.LivroRepository;
import com.desafio.livraria.service.impl.LivroServiceImpl;
import com.desafio.livraria.validates.AutorValidates;
import com.desafio.livraria.validates.GeneroValidates;
import com.desafio.livraria.validates.LivroValidates;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do LivroServiceImpl")
class LivroServiceImplTest {

    @Mock
    private LivroRepository repository;

    @Mock
    private LivroMapper mapper;

    @Mock
    private LivroValidates livroValidates;

    @Mock
    private AutorValidates autorValidates;

    @Mock
    private GeneroValidates generoValidates;

    private LivroService service;

    @BeforeEach
    void setUp() {
        service = new LivroServiceImpl(repository, mapper, livroValidates, autorValidates, generoValidates);
    }

    @Test
    @DisplayName("Deve criar livro com sucesso")
    void criarLivro_shouldCreateLivroSuccessfully() {
        // Given
        UUID autorId = UUID.randomUUID();
        UUID generoId = UUID.randomUUID();
        
        LivroRequestDTO requestDTO = new LivroRequestDTO();
        requestDTO.setTitulo("O Senhor dos Anéis");
        requestDTO.setAutorId(autorId);
        requestDTO.setGeneroId(generoId);

        Autor autor = new Autor();
        autor.setNome("J.R.R. Tolkien");
        
        Genero genero = new Genero();
        genero.setNome("Fantasia");

        Livro livro = new Livro();
        livro.setTitulo("O Senhor dos Anéis");
        livro.setAutor(autor);
        livro.setGenero(genero);

        LivroResponseDTO responseDTO = new LivroResponseDTO();
        responseDTO.setTitulo("O Senhor dos Anéis");

        when(autorValidates.validarAutorExistePorId(autorId)).thenReturn(autor);
        when(generoValidates.validarGeneroExistePorId(generoId)).thenReturn(genero);
        when(repository.save(any(Livro.class))).thenReturn(livro);
        when(mapper.toResponseDTO(livro)).thenReturn(responseDTO);

        // When
        LivroResponseDTO result = service.criarLivro(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitulo()).isEqualTo("O Senhor dos Anéis");
        verify(livroValidates).validarTituloLivroNaoExiste("O Senhor dos Anéis");
        verify(autorValidates).validarAutorExistePorId(autorId);
        verify(generoValidates).validarGeneroExistePorId(generoId);
        verify(repository).save(any(Livro.class));
    }

    @Test
    @DisplayName("Deve buscar livro por ID com sucesso")
    void buscarPorId_shouldReturnLivro_whenExists() {
        // Given
        UUID id = UUID.randomUUID();
        Livro livro = new Livro();
        livro.setTitulo("O Senhor dos Anéis");

        LivroResponseDTO responseDTO = new LivroResponseDTO();
        responseDTO.setTitulo("O Senhor dos Anéis");

        when(livroValidates.validarLivroExistePorId(id)).thenReturn(livro);
        when(mapper.toResponseDTO(livro)).thenReturn(responseDTO);

        // When
        LivroResponseDTO result = service.buscarPorId(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitulo()).isEqualTo("O Senhor dos Anéis");
        verify(livroValidates).validarLivroExistePorId(id);
    }

    @Test
    @DisplayName("Deve listar todos os livros")
    void listarTodos_shouldReturnAllLivros() {
        // Given
        Livro livro1 = new Livro();
        livro1.setTitulo("O Senhor dos Anéis");
        Livro livro2 = new Livro();
        livro2.setTitulo("O Hobbit");

        LivroResponseDTO response1 = new LivroResponseDTO();
        response1.setTitulo("O Senhor dos Anéis");
        LivroResponseDTO response2 = new LivroResponseDTO();
        response2.setTitulo("O Hobbit");

        when(repository.findAll()).thenReturn(List.of(livro1, livro2));
        when(mapper.toResponseDTO(livro1)).thenReturn(response1);
        when(mapper.toResponseDTO(livro2)).thenReturn(response2);

        // When
        List<LivroResponseDTO> result = service.listarTodos();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitulo()).isEqualTo("O Senhor dos Anéis");
        assertThat(result.get(1).getTitulo()).isEqualTo("O Hobbit");
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve atualizar livro com sucesso")
    void atualizarLivro_shouldUpdateLivroSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();
        UUID autorId = UUID.randomUUID();
        UUID generoId = UUID.randomUUID();
        
        LivroRequestDTO requestDTO = new LivroRequestDTO();
        requestDTO.setTitulo("O Senhor dos Anéis Atualizado");
        requestDTO.setAutorId(autorId);
        requestDTO.setGeneroId(generoId);

        Livro livroExistente = new Livro();
        livroExistente.setTitulo("O Senhor dos Anéis");

        Autor autor = new Autor();
        autor.setNome("J.R.R. Tolkien");
        
        Genero genero = new Genero();
        genero.setNome("Fantasia");

        LivroResponseDTO responseDTO = new LivroResponseDTO();
        responseDTO.setTitulo("O Senhor dos Anéis Atualizado");

        when(livroValidates.validarLivroExistePorId(id)).thenReturn(livroExistente);
        when(autorValidates.validarAutorExistePorId(autorId)).thenReturn(autor);
        when(generoValidates.validarGeneroExistePorId(generoId)).thenReturn(genero);
        when(repository.save(any(Livro.class))).thenReturn(livroExistente);
        when(mapper.toResponseDTO(livroExistente)).thenReturn(responseDTO);

        // When
        LivroResponseDTO result = service.atualizarLivro(id, requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitulo()).isEqualTo("O Senhor dos Anéis Atualizado");
        verify(livroValidates).validarLivroExistePorId(id);
        verify(livroValidates).validarTituloLivroNaoExisteParaAtualizacao("O Senhor dos Anéis Atualizado", id);
        verify(autorValidates).validarAutorExistePorId(autorId);
        verify(generoValidates).validarGeneroExistePorId(generoId);
        verify(repository).save(livroExistente);
    }

    @Test
    @DisplayName("Deve remover livro com sucesso")
    void removerLivro_shouldRemoveLivroSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        service.removerLivro(id);

        // Then
        verify(livroValidates).validarLivroExisteParaRemocao(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Deve buscar livro por título com sucesso")
    void buscarPorTitulo_shouldReturnLivro_whenExists() {
        // Given
        String titulo = "O Senhor dos Anéis";
        Livro livro = new Livro();
        livro.setTitulo("O Senhor dos Anéis");

        LivroResponseDTO responseDTO = new LivroResponseDTO();
        responseDTO.setTitulo("O Senhor dos Anéis");

        when(livroValidates.validarLivroExistePorTitulo(titulo)).thenReturn(livro);
        when(mapper.toResponseDTO(livro)).thenReturn(responseDTO);

        // When
        LivroResponseDTO result = service.buscarPorTitulo(titulo);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitulo()).isEqualTo("O Senhor dos Anéis");
        verify(livroValidates).validarLivroExistePorTitulo(titulo);
    }
}

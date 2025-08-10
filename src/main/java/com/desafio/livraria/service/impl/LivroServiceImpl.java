package com.desafio.livraria.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.livraria.dto.request.LivroRequestDTO;
import com.desafio.livraria.dto.response.LivroResponseDTO;
import com.desafio.livraria.mapper.LivroMapper;
import com.desafio.livraria.model.Livro;
import com.desafio.livraria.model.Autor;
import com.desafio.livraria.model.Genero;
import com.desafio.livraria.repository.LivroRepository;
import com.desafio.livraria.service.LivroService;
import com.desafio.livraria.validates.LivroValidates;
import com.desafio.livraria.validates.AutorValidates;
import com.desafio.livraria.validates.GeneroValidates;

@Service
@Transactional
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;
    private final LivroValidates livroValidates;
    private final AutorValidates autorValidates;
    private final GeneroValidates generoValidates;

    public LivroServiceImpl(LivroRepository livroRepository,
                            LivroMapper livroMapper,
                            LivroValidates livroValidates,
                            AutorValidates autorValidates,
                            GeneroValidates generoValidates) {
        this.livroRepository = livroRepository;
        this.livroMapper = livroMapper;
        this.livroValidates = livroValidates;
        this.autorValidates = autorValidates;
        this.generoValidates = generoValidates;
    }

    @Override
    public LivroResponseDTO criarLivro(LivroRequestDTO livroRequestDTO) {
        livroValidates.validarTituloLivroNaoExiste(livroRequestDTO.getTitulo());

        Autor autor = autorValidates.validarAutorExistePorId(livroRequestDTO.getAutorId());
        Genero genero = generoValidates.validarGeneroExistePorId(livroRequestDTO.getGeneroId());

        Livro livro = livroMapper.toEntity(livroRequestDTO,autor,genero);
        livro.setTitulo(livroValidates.normalizarTitulo(livroRequestDTO.getTitulo()));
        livro.setAutor(autor);
        livro.setGenero(genero);

        Livro livroSalvo = livroRepository.save(livro);
        return livroMapper.toResponseDTO(livroSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(UUID id) {
        Livro livro = livroValidates.validarLivroExistePorId(id);
        return livroMapper.toResponseDTO(livro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> listarTodos() {
        List<Livro> livros = livroRepository.findAll();
        return livros.stream()
                .map(livroMapper::toResponseDTO)
                .toList();
    }

    @Override
    public LivroResponseDTO atualizarLivro(UUID id, LivroRequestDTO livroRequestDTO) {
        Livro livroExistente = livroValidates.validarLivroExistePorId(id);
        livroValidates.validarTituloLivroNaoExisteParaAtualizacao(livroRequestDTO.getTitulo(), id);

        Autor autor = autorValidates.validarAutorExistePorId(livroRequestDTO.getAutorId());
        Genero genero = generoValidates.validarGeneroExistePorId(livroRequestDTO.getGeneroId());

        livroExistente.setTitulo(livroValidates.normalizarTitulo(livroRequestDTO.getTitulo()));
        livroExistente.setAutor(autor);
        livroExistente.setGenero(genero);

        Livro livroAtualizado = livroRepository.save(livroExistente);
        return livroMapper.toResponseDTO(livroAtualizado);
    }

    @Override
    public void removerLivro(UUID id) {
        livroValidates.validarLivroExisteParaRemocao(id);
        livroRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorTitulo(String titulo) {
        Livro livro = livroValidates.validarLivroExistePorTitulo(titulo);
        return livroMapper.toResponseDTO(livro);
    }
}

package com.desafio.livraria.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.livraria.dto.request.AutorRequestDTO;
import com.desafio.livraria.dto.response.AutorResponseDTO;
import com.desafio.livraria.mapper.AutorMapper;
import com.desafio.livraria.model.Autor;
import com.desafio.livraria.repository.AutorRepository;
import com.desafio.livraria.service.AutorService;
import com.desafio.livraria.validates.AutorValidates;

@Service
@Transactional
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;
    private final AutorValidates autorValidates;

    public AutorServiceImpl(AutorRepository autorRepository,
                            AutorMapper autorMapper,
                            AutorValidates autorValidates) {
        this.autorRepository = autorRepository;
        this.autorMapper = autorMapper;
        this.autorValidates = autorValidates;
    }

    @Override
    public AutorResponseDTO criarAutor(AutorRequestDTO autorRequestDTO) {
        autorValidates.validarNomeAutorNaoExiste(autorRequestDTO.getNome());

        Autor autor = autorMapper.toEntity(autorRequestDTO);
        autor.setNome(autorValidates.normalizarNome(autorRequestDTO.getNome()));

        Autor autorSalvo = autorRepository.save(autor);
        return autorMapper.toResponseDTO(autorSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(UUID id) {
        Autor autor = autorValidates.validarAutorExistePorId(id);
        return autorMapper.toResponseDTO(autor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listarTodos() {
        List<Autor> autores = autorRepository.findAll();

        return autores.stream()
                .map(autorMapper::toResponseDTO)
                .toList();
    }

    @Override
    public AutorResponseDTO atualizarAutor(UUID id, AutorRequestDTO autorRequestDTO) {
        Autor autorExistente = autorValidates.validarAutorExistePorId(id);
        autorValidates.validarNomeAutorNaoExisteParaAtualizacao(autorRequestDTO.getNome(), id);

        autorExistente.setNome(autorValidates.normalizarNome(autorRequestDTO.getNome()));

        Autor autorAtualizado = autorRepository.save(autorExistente);
        return autorMapper.toResponseDTO(autorAtualizado);
    }

    @Override
    public void removerAutor(UUID id) {
        autorValidates.validarAutorExisteParaRemocao(id);
        autorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorNome(String nome) {
        Autor autor = autorValidates.validarAutorExistePorNome(nome);
        return autorMapper.toResponseDTO(autor);
    }
}


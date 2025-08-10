package com.desafio.livraria.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.livraria.dto.request.GeneroRequestDTO;
import com.desafio.livraria.dto.response.GeneroResponseDTO;
import com.desafio.livraria.mapper.GeneroMapper;
import com.desafio.livraria.model.Genero;
import com.desafio.livraria.repository.GeneroRepository;
import com.desafio.livraria.service.GeneroService;
import com.desafio.livraria.validates.GeneroValidates;


@Service
@Transactional
public class GeneroServiceImpl implements GeneroService {
    
    private final GeneroRepository generoRepository;
    private final GeneroMapper generoMapper;
    private final GeneroValidates generoValidates;
    
    public GeneroServiceImpl(GeneroRepository generoRepository, 
                           GeneroMapper generoMapper, 
                           GeneroValidates generoValidates) {
        this.generoRepository = generoRepository;
        this.generoMapper = generoMapper;
        this.generoValidates = generoValidates;
    }
    
    @Override
    public GeneroResponseDTO criarGenero(GeneroRequestDTO generoRequestDTO) {
        generoValidates.validarNomeGeneroNaoExiste(generoRequestDTO.getNome());
        
        Genero genero = generoMapper.toEntity(generoRequestDTO);
        genero.setNome(generoValidates.normalizarNome(generoRequestDTO.getNome()));
        
        Genero generoSalvo = generoRepository.save(genero);
        return generoMapper.toResponseDTO(generoSalvo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public GeneroResponseDTO buscarPorId(UUID id) {
        Genero genero = generoValidates.validarGeneroExistePorId(id);
        return generoMapper.toResponseDTO(genero);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GeneroResponseDTO> listarTodos() {
        List<Genero> generos = generoRepository.findAll();
        
        return generos.stream()
                .map(generoMapper::toResponseDTO)
                .toList();
    }
    
    @Override
    public GeneroResponseDTO atualizarGenero(UUID id, GeneroRequestDTO generoRequestDTO) {
        Genero generoExistente = generoValidates.validarGeneroExistePorId(id);
        generoValidates.validarNomeGeneroNaoExisteParaAtualizacao(generoRequestDTO.getNome(), id);
        
        generoExistente.setNome(generoValidates.normalizarNome(generoRequestDTO.getNome()));
        
        Genero generoAtualizado = generoRepository.save(generoExistente);
        return generoMapper.toResponseDTO(generoAtualizado);
    }
    
    @Override
    public void removerGenero(UUID id) {
        generoValidates.validarGeneroExisteParaRemocao(id);
        generoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public GeneroResponseDTO buscarPorNome(String nome) {
        Genero genero = generoValidates.validarGeneroExistePorNome(nome);
        return generoMapper.toResponseDTO(genero);
    }
    

}

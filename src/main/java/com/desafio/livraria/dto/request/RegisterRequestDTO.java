package com.desafio.livraria.dto.request;

import com.desafio.livraria.model.UserRole;

public record RegisterRequestDTO(String login, String password, UserRole role) {

}

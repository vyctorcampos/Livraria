package com.desafio.livraria.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.desafio.livraria.model.Users;

public interface UserRepository extends JpaRepository<Users, UUID>{

	UserDetails findByLogin(String login);
}

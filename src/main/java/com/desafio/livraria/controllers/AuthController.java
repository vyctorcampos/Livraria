package com.desafio.livraria.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.livraria.config.security.TokenService;
import com.desafio.livraria.dto.request.AuthRequestDTO;
import com.desafio.livraria.dto.request.RegisterRequestDTO;
import com.desafio.livraria.dto.response.LoginResponseDTO;
import com.desafio.livraria.dto.response.RegisterRespondeDTO;
import com.desafio.livraria.model.Users;
import com.desafio.livraria.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private TokenService tokenService;
			
	
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Users) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(data.login(),token));
    }
	
	

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Users newUser = new Users(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);
        
        String token = tokenService.generateToken(newUser);

        return ResponseEntity.ok(new RegisterRespondeDTO(newUser.getLogin(), token));
    }
	
}

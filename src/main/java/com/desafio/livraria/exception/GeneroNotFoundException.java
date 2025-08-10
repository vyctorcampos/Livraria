package com.desafio.livraria.exception;

/**
 * Exceção lançada quando um gênero não é encontrado
 */
public class GeneroNotFoundException extends RuntimeException {
    
    public GeneroNotFoundException(String message) {
        super(message);
    }
    
    public GeneroNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

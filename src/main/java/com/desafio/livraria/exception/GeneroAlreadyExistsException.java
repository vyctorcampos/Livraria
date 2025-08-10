package com.desafio.livraria.exception;

/**
 * Exceção lançada quando um gênero já existe
 */
public class GeneroAlreadyExistsException extends RuntimeException {
    
    public GeneroAlreadyExistsException(String message) {
        super(message);
    }
    
    public GeneroAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

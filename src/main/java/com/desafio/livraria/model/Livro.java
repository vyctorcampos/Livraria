package com.desafio.livraria.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@Table(name = "Livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false) 
    private String titulo;

    @ManyToOne
    @JoinColumn(
        name = "autor_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "fk_livro_autor")
    )
    private Autor autor;

    @ManyToOne
    @JoinColumn(
        name = "genero_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_livro_genero")
    )
    private Genero genero;
}
package br.senai.sc.security_consulta.model.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "livro")
@Data
public class Livro {

    @Id
    @Column(length = 13, nullable = false, unique = true)
    private Long isbn;

    @Column(length = 50, nullable = false)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "cpf_autor")
    private Autor autor;

    @Column(nullable = false)
    private Integer qtdPag;

    @ManyToOne
    @JoinColumn(name = "cpf_revisor")
    private Revisor revisor;

}

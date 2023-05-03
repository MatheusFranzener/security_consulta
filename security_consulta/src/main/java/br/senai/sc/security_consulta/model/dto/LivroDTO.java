package br.senai.sc.security_consulta.model.dto;

import br.senai.sc.security_consulta.model.entities.Autor;
import br.senai.sc.security_consulta.model.entities.Revisor;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class LivroDTO {

    @NonNull
    private Long isbn;

    @NonNull
    private String titulo;

    @NonNull
    private Integer qtdPag;

    private Autor autor;

    private Revisor revisor;

}

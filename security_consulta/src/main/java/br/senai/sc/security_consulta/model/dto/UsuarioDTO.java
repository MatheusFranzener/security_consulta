package br.senai.sc.security_consulta.model.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class UsuarioDTO {

    @NonNull
    private String nome;

    @NonNull
    private String email;

    @NonNull
    private String senha;

}

package br.senai.sc.security_consulta.security.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenDTO {

    @NotNull
    private String tipo;

    @NotNull
    private String token;

}

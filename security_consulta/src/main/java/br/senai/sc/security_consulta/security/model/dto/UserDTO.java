package br.senai.sc.security_consulta.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NonNull
    private String email;

    @NonNull
    private String senha;

}

package br.senai.sc.security_consulta.security.utils;

import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import br.senai.sc.security_consulta.security.model.exceptions.TokenInvalido;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    private final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";

    public String gerarToken(UserJpa usuario) {
        return Jwts.builder()
                .setIssuer("Editora de Livros")
                .setSubject(usuario.getUsuario().getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1800000))
                .signWith(SignatureAlgorithm.HS256, senhaForte)
                .compact();
    }

    public void validarToken(String token) throws TokenInvalido {
        try {
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
        } catch (Exception e) {
            throw new TokenInvalido();
        }
    }

    public Long getCpf(String token) {
        return Long.parseLong(Jwts.parser().
                setSigningKey(senhaForte).
                parseClaimsJws(token).
                getBody().
                getSubject());
    }

}

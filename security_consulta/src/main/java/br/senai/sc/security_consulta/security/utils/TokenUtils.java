package br.senai.sc.security_consulta.security.utils;

import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import br.senai.sc.security_consulta.security.model.exceptions.TokenInvalido;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.util.Date;

public class TokenUtils {

    private final String senhaForte = "05a9e62653eb0eaa116a1b8bbc06dd30ab0df73ab8ae16a500c80875e6e6c8a9";

    public String gerarToken(UserJpa usuario) {
        return Jwts.builder()
                .setIssuer("Editora Livros")
                .setSubject(usuario.getUsuario().getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 36000000))
                .signWith(SignatureAlgorithm.HS256, senhaForte)
                .compact();
    }

    public void validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
        } catch (Exception e) {
            throw new RuntimeException("Token Inválido!");
        }
    }

}

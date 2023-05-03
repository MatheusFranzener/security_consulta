package br.senai.sc.security_consulta.security.model.exceptions;

public class TokenInvalido extends Exception{
    public TokenInvalido() {
        super("Token inválido!");
    }
}

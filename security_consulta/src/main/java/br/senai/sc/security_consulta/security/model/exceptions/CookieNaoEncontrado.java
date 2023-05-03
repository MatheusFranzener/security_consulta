package br.senai.sc.security_consulta.security.model.exceptions;

public class CookieNaoEncontrado extends Exception{
    public CookieNaoEncontrado() {
        super("Cookie não encontrado!");
    }
}

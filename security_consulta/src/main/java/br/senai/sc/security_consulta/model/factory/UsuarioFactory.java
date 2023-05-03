package br.senai.sc.security_consulta.model.factory;

import br.senai.sc.security_consulta.model.entities.Autor;
import br.senai.sc.security_consulta.model.entities.Revisor;
import br.senai.sc.security_consulta.model.entities.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UsuarioFactory {
    public Usuario getUsuario(SimpleGrantedAuthority authority, Usuario pessoa) {
        switch (authority.getAuthority()) {
            case "Revisor" -> {
                Revisor revisor = new Revisor();
                revisor.setNome(pessoa.getNome());
                revisor.setEmail(pessoa.getEmail());
                revisor.setSenha(pessoa.getSenha());
                return revisor;
            }
            case "Autor" -> {
                Autor autor = new Autor();
                autor.setNome(pessoa.getNome());
                autor.setEmail(pessoa.getEmail());
                autor.setSenha(pessoa.getSenha());
                return autor;
            }
        }

        throw new IllegalArgumentException("Pessoa não encontrada");
    }
}

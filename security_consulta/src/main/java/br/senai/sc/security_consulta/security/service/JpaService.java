package br.senai.sc.security_consulta.security.service;

import br.senai.sc.security_consulta.model.entities.Usuario;
import br.senai.sc.security_consulta.repository.UsuarioRepository;
import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(username);

        if (usuarioOptional.isPresent()) {
            return new UserJpa(usuarioOptional.get());
        }

        throw new UsernameNotFoundException("Usuário não encontrado!");
    }

    // No caso se for necessário utilizar outro método de buscar, por exemplo cpf ou id:

    public UserDetails getUser(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return loadUserByUsername(usuario.getEmail());
        }

        throw new UsernameNotFoundException("Usuário não encontrado!");
    }

}

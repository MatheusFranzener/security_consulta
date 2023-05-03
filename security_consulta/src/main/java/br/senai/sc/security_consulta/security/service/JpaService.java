package br.senai.sc.security_consulta.security.service;

import br.senai.sc.security_consulta.model.entities.Usuario;
import br.senai.sc.security_consulta.repository.UsuarioRepository;
import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JpaService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Função que busca o usuário no banco de dados caso ele exista, prosseguindo com a autenticação
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(username);

        if(usuarioOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }

        return new UserJpa(usuarioOptional.get());
    }

}

package br.senai.sc.security_consulta.security.model.entities;

import br.senai.sc.security_consulta.model.entities.Usuario;
import br.senai.sc.security_consulta.security.utils.UserJpaDesializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = UserJpaDesializer.class)
public class UserJpa implements UserDetails {

    private Usuario usuario;

    private Collection<GrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private String password;

    private String username;

    public UserJpa(Usuario usuario) {
        this.usuario = usuario;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.password = usuario.getSenha();
        this.username = usuario.getEmail();
        this.authorities = new ArrayList<>();
        this.authorities.add(new SimpleGrantedAuthority(usuario.getClass().getSimpleName()));
    }

}

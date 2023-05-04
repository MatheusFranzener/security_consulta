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
@JsonDeserialize(using = UserJpaDesializer.class) // Apenas usar se der erro quando for fazer determinada permissão ( ex: cadastrar somente o usuário e não der certo )
public class UserJpa implements UserDetails {

    // Se tiver um erro tipo esse: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `org.springframework.security.core.GrantedAuthority` (no Creators, like default constructor, exist): abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information
    // at [Source: (String)"{"usuario":{"id":1,"nome":"Autor","email":"autor@gmail","senha":"123"},"authorities":[{"authority":"Autor"}],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true
    // Usar o JsonDeserialize

    // e se tiver um erro desse: javax.xml.bind.DatatypeConverter, usar a ultima dependencia que esta no pom.xml

    private Usuario usuario;

    private Collection<GrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private String password;

    private String username;

    // Criando um construtor mais facilitado para o Spring Security
    public UserJpa(Usuario usuario) {
        this.usuario = usuario;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.password = usuario.getSenha();
        this.username = usuario.getEmail();
        this.authorities = new ArrayList<>();
        // questão do tipo do usuário
        this.authorities.add(new SimpleGrantedAuthority(usuario.getClass().getSimpleName()));
    }

}

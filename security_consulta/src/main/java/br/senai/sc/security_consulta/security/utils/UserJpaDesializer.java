package br.senai.sc.security_consulta.security.utils;

import br.senai.sc.security_consulta.model.entities.Usuario;
import br.senai.sc.security_consulta.model.factory.UsuarioFactory;
import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Classe para transformar o JSON em um objeto do tipo UserJpa
// Utilizada somente se der algum erro nas permissões quando vai fazer algo
public class UserJpaDesializer extends JsonDeserializer<UserJpa> {

    @Override
    public UserJpa deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        ArrayNode authoritiesNode = (ArrayNode) node.get("authorities");

        for (JsonNode authorityNode : authoritiesNode) {
            String authority = authorityNode.get("authority").asText();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
            authorities.add(simpleGrantedAuthority);
        }

        Usuario usuario = mapper.convertValue(node.get("usuario"), Usuario.class);

        SimpleGrantedAuthority authority = authorities.get(0);
        usuario = new UsuarioFactory().getUsuario(authority, usuario);

        return new UserJpa(usuario);
    }

}

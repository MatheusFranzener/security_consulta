package br.senai.sc.security_consulta.security.controller;

import br.senai.sc.security_consulta.security.model.dto.UserDTO;
import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import br.senai.sc.security_consulta.security.utils.CookieUtils;
import br.senai.sc.security_consulta.security.utils.TokenUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// Controller usado para as requisições de autenticação
@RestController
@RequestMapping("/editora-livros/login")
public class AutenticacaoController {

    private TokenUtils tokenUtils = new TokenUtils();

    private CookieUtils cookieUtils = new CookieUtils();

    @Autowired
    private AuthenticationManager authenticationManager;

    // Método POST para a realização da autenticação, recebendo um userDTO como body da requisição
    @PostMapping("/auth")
    public ResponseEntity<Object> autenticacao(@RequestBody @Valid UserDTO userDTO, HttpServletResponse response) {
        // Verificando se o usuário existe e se a senha está correta
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getSenha());

        // Se o usuário existir e a senha estiver correta, é feita a autenticação
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // Se a autenticacao acontecer, é gerado um token e um cookie com o token e o usuário
        if (authentication.isAuthenticated()) {
            UserJpa userJpa = (UserJpa) authentication.getPrincipal();

            response.addCookie(cookieUtils.gerarTokenCookie(userJpa));
            response.addCookie(cookieUtils.gerarUserCookie(userJpa));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userJpa.getUsername(),
                            userJpa.getPassword(), userJpa.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            return ResponseEntity.status(HttpStatus.OK).body(userJpa.getUsuario());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}

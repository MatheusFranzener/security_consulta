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

@RestController
@RequestMapping("/editora-livros/login")
public class AutenticacaoController {

    private TokenUtils tokenUtils = new TokenUtils();

    private CookieUtils cookieUtils = new CookieUtils();

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<Object> autenticacao(@RequestBody @Valid UserDTO userDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getSenha());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (authentication.isAuthenticated()) {
            UserJpa userJpa = (UserJpa) authentication.getPrincipal();

            response.addCookie(cookieUtils.gerarTokenCookie(userJpa));
            response.addCookie(cookieUtils.gerarUserCookie(userJpa));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userJpa.getUsername(),
                            userJpa.getPassword(), userJpa.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(
                    usernamePasswordAuthenticationToken);

            return ResponseEntity.status(HttpStatus.OK).body(userJpa.getUsuario());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}

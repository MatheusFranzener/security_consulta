package br.senai.sc.security_consulta.security.controller;

import br.senai.sc.security_consulta.security.model.dto.UserDTO;
import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import br.senai.sc.security_consulta.security.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/editora-livros/login")
@CrossOrigin
@AllArgsConstructor
public class AutenticacaoController {

    private AuthenticationManager authenticationManager;

    private final CookieUtils cookieUtils = new CookieUtils();

    @PostMapping("/auth")
    public ResponseEntity<Object> autenticacao(@RequestBody UserDTO usuarioDTO, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {
                UserJpa user = (UserJpa) authentication.getPrincipal();

                Cookie jwtCookie = cookieUtils.gerarTokenCookie(user);
                response.addCookie(jwtCookie);

                Cookie userCookie = cookieUtils.gerarUserCookie(user);
                response.addCookie(userCookie);

                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            System.out.println("Erro ao autenticar usuário");
            System.out.println(e);

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

}

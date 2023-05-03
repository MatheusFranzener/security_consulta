package br.senai.sc.security_consulta.security.filter;

import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import br.senai.sc.security_consulta.security.service.JpaService;
import br.senai.sc.security_consulta.security.utils.CookieUtils;
import br.senai.sc.security_consulta.security.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {

    private CookieUtils cookieUtils;

    private TokenUtils tokenUtils;

    private JpaService jpaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals("/editora-livros/login") || request.getRequestURI().equals("/editora-livros/login/auth") || request.getRequestURI().equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = cookieUtils.getTokenCookie(request);
            tokenUtils.validarToken(token);
            UserJpa user = cookieUtils.getUserCookie(request);

            cookieUtils.renovarCookie(request, "jwt");
            cookieUtils.renovarCookie(request, "user");

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }



}

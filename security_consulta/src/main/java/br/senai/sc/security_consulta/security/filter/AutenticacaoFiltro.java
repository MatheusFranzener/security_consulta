package br.senai.sc.security_consulta.security.filter;

import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import br.senai.sc.security_consulta.security.model.exceptions.CookieNaoEncontrado;
import br.senai.sc.security_consulta.security.model.exceptions.TokenInvalido;
import br.senai.sc.security_consulta.security.model.exceptions.UrlNaoPermitida;
import br.senai.sc.security_consulta.security.utils.CookieUtils;
import br.senai.sc.security_consulta.security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {

    private final CookieUtils cookieUtils = new CookieUtils();

    private final JwtUtils jwtUtils = new JwtUtils();

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = cookieUtils.getTokenCookie(request);
            jwtUtils.validarToken(token);

            UserJpa user = cookieUtils.getUserCookie(request);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

        } catch (CookieNaoEncontrado | TokenInvalido e) {
            e.printStackTrace();

            try {
                validarUrl(request.getRequestURI());
            } catch (UrlNaoPermitida urlNaoPermitida) {
                urlNaoPermitida.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private void validarUrl(String url) throws UrlNaoPermitida {
        if (!(url.equals("/editora-livros/login/auth")
                || url.equals("/editora-livros/logout")
                || url.equals("http://localhost:3000/login")
                || url.equals("https://localhost:3000/login")
                || url.startsWith("/api-docs")
                || url.startsWith("/swagger")
        )) {
            System.out.println("URL não permitida: " + url);
            throw new UrlNaoPermitida();
        }

        System.out.println("URL permitida: " + url);
    }

}

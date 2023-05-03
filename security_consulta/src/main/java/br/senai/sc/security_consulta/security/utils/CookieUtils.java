package br.senai.sc.security_consulta.security.utils;

import br.senai.sc.security_consulta.security.model.entities.UserJpa;
import br.senai.sc.security_consulta.security.model.exceptions.CookieNaoEncontrado;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CookieUtils {

    private final JwtUtils jwtUtils = new JwtUtils();

    public Cookie gerarTokenCookie(UserJpa userJpa) {
        String token = jwtUtils.gerarToken(userJpa);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setPath("/");
        cookie.setMaxAge(1800);

        return cookie;
    }

    public String getTokenCookie(HttpServletRequest request) throws CookieNaoEncontrado {
        try {
            Cookie cookie = WebUtils.getCookie(request, "jwt");
            return cookie.getValue();
        } catch (Exception e) {
            throw new CookieNaoEncontrado();
        }
    }

    public Cookie gerarUserCookie(UserJpa userJpa) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = URLEncoder.encode(objectMapper.writeValueAsString(userJpa), StandardCharsets.UTF_8);

            Cookie cookie = new Cookie("user", userJson);
            cookie.setPath("/");
            cookie.setMaxAge(1800);

            return cookie;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Cookie renovarCookie(HttpServletRequest request, String nome) {
        Cookie cookie = WebUtils.getCookie(request, nome);
        cookie.setPath("/");
        cookie.setMaxAge(1800);

        return cookie;
    }

    public UserJpa getUserCookie(HttpServletRequest request) throws CookieNaoEncontrado {
        try {
            Cookie cookie = WebUtils.getCookie(request, "user");

            String jsonUser = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(jsonUser, UserJpa.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CookieNaoEncontrado();
        }
    }

}

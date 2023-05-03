package br.senai.sc.security_consulta.security.config;

import br.senai.sc.security_consulta.security.filter.AutenticacaoFiltro;
import br.senai.sc.security_consulta.security.service.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class AutenticacaoConfig {

    @Autowired
    private JpaService jpaService;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jpaService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        configuration.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE"));

        configuration.setAllowCredentials(true);

        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/editora-livros/login/auth",
                        "/editora-livros/login",
                        "/api-docs/**",
                        "/swagger.html",
                        "/swagger-ui/**"
                ).permitAll()

                .requestMatchers(HttpMethod.POST, "/editora-livros/livro").hasAuthority("Autor")
                .requestMatchers(HttpMethod.POST, "/editora-livros/usuario").hasAnyAuthority("Autor", "Revisor")
                .requestMatchers(HttpMethod.GET, "/editora-livros/usuario/{id}").hasAnyAuthority("Autor", "Revisor")
                .requestMatchers(HttpMethod.GET, "/editora-livros/livro").hasAuthority("Autor")
                .requestMatchers(HttpMethod.DELETE, "/editora-livros/livro/{isbn}").hasAuthority("Revisor")
                .anyRequest().authenticated();

        http.csrf().disable();

        http.cors().configurationSource(corsConfigurationSource());

        http.logout()
                .deleteCookies("jwt", "user")
                .permitAll();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoFiltro(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}

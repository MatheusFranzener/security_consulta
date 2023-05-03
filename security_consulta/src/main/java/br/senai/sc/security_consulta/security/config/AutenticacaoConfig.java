package br.senai.sc.security_consulta.security.config;

import br.senai.sc.security_consulta.security.filter.AutenticacaoFiltro;
import br.senai.sc.security_consulta.security.service.JpaService;
import br.senai.sc.security_consulta.security.utils.CookieUtils;
import br.senai.sc.security_consulta.security.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// Classe de configuração geral do sistema
@Configuration
@AllArgsConstructor
public class AutenticacaoConfig {

    // Criado para usar o nosso usuário e nossos dados ( caso contrário usaria o padrão ( userDetailsService))
    private JpaService jpaService;


    // Configurações do serviço utilizado para autenticação e encriptação de senhas
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jpaService).passwordEncoder(new BCryptPasswordEncoder());
    }

    // Criação das configurações que serão usadas no cors
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permitindo todas as origins que irão acessar o sistema
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // Permitindo todos os métodos que serão usados
        configuration.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE"));

        // Permissão para salvar e capturar os cookies da aplicação
        configuration.setAllowCredentials(true);

        // Permitindo todos os headers possíveis
        configuration.setAllowedHeaders(List.of("*"));

        // Registra a configuração para todos os caminhos da nossa aplicação
        // Também posso permitir o acesso ao cors para apenas um caminho como por exemplo "/login"
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // Configurando as permissões de acesso para cada caminho da aplicação
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Permitindo as urls que não precisam de autenticação
        http.authorizeHttpRequests()
                .requestMatchers("/editora-livros/login/auth",
                        "/editora-livros/login",
                        "/editora-livros/usuario",
                        "/api-docs/**",
                        "/swagger.html",
                        "/swagger-ui/**"
                ).permitAll()

                // Depois fazendo a configuração das que necessitam de autenticação e também permissão para um determinado tipo de usuário
                .requestMatchers(HttpMethod.POST, "/editora-livros/livro").hasAuthority("Autor")
                .requestMatchers(HttpMethod.GET, "/editora-livros/usuario/{id}").hasAnyAuthority("Autor", "Revisor")
                .requestMatchers(HttpMethod.GET, "/editora-livros/livro").hasAuthority("Autor")
                .requestMatchers(HttpMethod.DELETE, "/editora-livros/livro/{isbn}").hasAuthority("Revisor")
                .anyRequest().authenticated();

        // Desabilitando a política do csrf
        http.csrf().disable();

        // Habilitando e colocando a nossa política do cors
        http.cors().configurationSource(corsConfigurationSource());

        // Permitindo o logout e excluindo os cookies
        http.logout()
                .deleteCookies("jwt", "user")
                .permitAll();

        // Cria uma política de sessão ( usuário não fique autenticado )
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Faz com que toda vez que há uma requisição passe antes pelo filtro
        http.addFilterBefore(new AutenticacaoFiltro(new CookieUtils(), new TokenUtils(), jpaService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Injeção de dependências no AutenticacaoController quando necessitar (autowired não funcionar)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}

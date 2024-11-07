package com.webservices.desafio.Auth.Config;

import com.webservices.desafio.Auth.JWT.AuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableMethodSecurity
@Configuration
public class SpringSecurityConfig {

    // Define a configuração do SecurityFilterChain para gerenciar a segurança da aplicação
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desativa a proteção CSRF (Cross-Site Request Forgery)
                .formLogin(form -> form.disable()) // Desativa o formulário de login padrão do Spring Security
                .httpBasic(basic -> basic.disable()) // Desativa a autenticação HTTP Basic
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll() // Permite requisições POST para qualquer endpoint sob /api/**
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll() // Permite requisições GET para qualquer endpoint sob /api/**
                        .requestMatchers(HttpMethod.POST, "/login").permitAll() // Permite requisições POST para o endpoint de login
                        .requestMatchers(HttpMethod.PUT, "/api/**").authenticated() // Requer autenticação para requisições PUT sob /api/**
                        .anyRequest().authenticated()) // Requer autenticação para qualquer outra requisição
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura a política de criação de sessão como STATELESS
                .build(); // Retorna o SecurityFilterChain configurado
    }

    // Define o gerenciador de autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager(); // Retorna o gerenciador de autenticação padrão configurado
    }

    // Define o filtro de autorização JWT
    @Bean
    public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter(); // Retorna uma nova instância do filtro de autorização
    }
}


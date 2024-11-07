package com.webservices.desafio.Auth;

import com.webservices.desafio.Auth.JWT.JwtToken;
import com.webservices.desafio.Auth.JWT.JwtUtil;
import com.webservices.desafio.Entities.Person;
import com.webservices.desafio.web.Repository;
import com.webservices.desafio.web.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class JwtUserDetailsService implements UserDetailsService {

    // Repositório injetado para buscar dados de usuários
    private final Repository repository;

    // Metodo para carregar o usuário pelo nome de usuário
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário no repositório; lança exceção se não for encontrado
        Person user = repository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(user); // Retorna uma instância de User com base na entidade Person
    }

    // Metodo para gerar um token JWT autenticado com o nome de usuário e função (role)
    public JwtToken getTokenAuthenticated(String username, String role) {
        return JwtUtil.createToken(username, role); // Utiliza o utilitário JwtUtil para criar o token
    }
}

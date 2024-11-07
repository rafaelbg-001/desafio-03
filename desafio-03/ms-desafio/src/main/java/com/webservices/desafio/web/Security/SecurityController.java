package com.webservices.desafio.web.Security;

import com.webservices.desafio.Auth.JWT.JwtToken;
import com.webservices.desafio.Auth.JWT.JwtUtil;
import com.webservices.desafio.Auth.JwtUserDetailsService;
import com.webservices.desafio.Auth.User;
import com.webservices.desafio.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SecurityController {

    private final JwtUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    // Metodo responsável por autenticar o usuário e gerar um token JWT
    @PostMapping(value = "/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequest login, HttpServletRequest request) {
        try {
            // Tenta autenticar o usuário com as credenciais fornecidas
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

            // Após a autenticação bem-sucedida, carrega o usuário pelo nome de usuário
            User user = userDetailsService.loadUserByUsername(login.getUsername());

            // Gera um token JWT com base no nome de usuário e nas autoridades do usuário
            JwtToken token = userDetailsService.getTokenAuthenticated(user.getUsername(), user.getAuthorities().toString());

            // Valida o token gerado
            JwtUtil.isTokenValid(token.getToken());

            // Retorna uma resposta de sucesso com o token gerado
            return ResponseEntity.ok().body("Token: Bearer " + token.getToken());
        } catch (AuthenticationException e) {
            // Caso a autenticação falhe, registra um aviso no log
            log.warn("Bad Credentials for " + login.getUsername());
        }

        // Retorna uma resposta de erro caso as credenciais estejam incorretas
        return ResponseEntity.badRequest().build();
    }
}


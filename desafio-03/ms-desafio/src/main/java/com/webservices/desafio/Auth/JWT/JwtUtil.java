package com.webservices.desafio.Auth.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtil {
    public static final String Jwt_Bearer = "Bearer ";
    public static final String Jwt_Authorization = "Authorization";
    public static final String SECRET_KEY = "rcccc1123131231casdadasdadas12345678910";  // Chave secreta para assinatura do JWT
    public static final long expireDays = 0;  // Dias de expiração do token
    public static final long expireHours = 1;  // Horas de expiração do token
    public static final long expireMinutes = 2;  // Minutos de expiração do token

    // Construtor privado para evitar instâncias dessa classe utilitária
    private JwtUtil(){}

    // Metodo que gera a chave secreta para assinar o token JWT
    private static javax.crypto.SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Metodo que calcula a data de expiração do token a partir de uma data de início
    private static Date toExpireDate(Date start){
        // Converte a data de início para LocalDateTime
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Adiciona os dias, horas e minutos para calcular a expiração
        LocalDateTime end = dateTime.plusDays(expireDays).plusHours(expireHours).plusMinutes(expireMinutes);

        // Retorna a data final de expiração convertida para o tipo Date
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Metodo responsável por criar um token JWT com o nome de usuário e o papel (role)
    public static JwtToken createToken(String username, String role) {
        // Data de emissão do token
        Date issuedAt = new Date();

        // Calcula a data de expiração
        Date limit = toExpireDate(issuedAt);

        // Cria o token JWT com as informações passadas
        String token = Jwts.builder()
                .header().add("typ", "JWT")  // Adiciona o tipo de token
                .and()
                .subject(username)  // Define o nome de usuário como o sujeito do token
                .issuedAt(issuedAt)  // Define a data de emissão
                .expiration(limit)  // Define a data de expiração
                .signWith(generateKey())  // Assina o token com a chave secreta
                .claim("Role", role)  // Adiciona a claim do papel (role) do usuário
                .compact();  // Gera o token compactado

        // Retorna o token dentro de um objeto JwtToken
        return new JwtToken(token);
    }

    // Metodo que extrai as claims do token JWT
    private static Claims getClaimsFromToken(String token) {
        try {
            // Analisa e valida o token, verificando a assinatura com a chave secreta
            return Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token)).getPayload();
        } catch (JwtException ex) {
            // Caso ocorra um erro ao processar o token, loga a falha
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return null;
    }

    // Metodo que verifica se o token JWT é válido
    public static boolean isTokenValid(String token) {
        try {
            // Tenta validar o token verificando sua assinatura
            Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token));

            // Caso o token seja válido, loga e retorna true
            log.info("Token valido");
            return true;
        } catch (JwtException ex) {
            // Caso o token seja inválido, loga a falha e retorna false
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return false;
    }

    // Metodo que remove o prefixo "Bearer " do token, se presente
    private static String refactorToken(String token){
        // Verifica se o token começa com o prefixo "Bearer "
        if (token.contains(Jwt_Bearer)){
            // Se sim, retorna o token sem esse prefixo
            return token.substring(Jwt_Bearer.length());
        }
        return token;  // Caso contrário, retorna o token original
    }

    // Metodo que extrai o nome de usuário do token JWT
    public static String getUsernameToken (String token){
        // Obtém as claims do token e retorna o sujeito (username)
        return getClaimsFromToken(token).getSubject();
    }
}


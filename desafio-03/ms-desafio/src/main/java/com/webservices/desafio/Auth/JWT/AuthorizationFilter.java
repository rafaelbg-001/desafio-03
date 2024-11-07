package com.webservices.desafio.Auth.JWT;

import com.webservices.desafio.Auth.JwtUserDetailsService;
import com.webservices.desafio.Auth.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token = request.getHeader(JwtUtil.Jwt_Authorization);
        if(token == null || !token.startsWith(JwtUtil.Jwt_Bearer)){
            log.info("JWT is null or does not have Bearer");

            filterChain.doFilter(request, response);
            return;
        }

        if(!JwtUtil.isTokenValid(token)){
            log.warn("JWT is not valid or expired");

            filterChain.doFilter(request, response);
            return;
        }

        String username = JwtUtil.getUsernameToken(token);

        toAuthentication(request, username);

        filterChain.doFilter(request, response);
    }

    private void toAuthentication(HttpServletRequest request, String username) {
        User user = service.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


}

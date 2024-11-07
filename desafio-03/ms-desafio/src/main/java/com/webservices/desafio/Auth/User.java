package com.webservices.desafio.Auth;

import com.webservices.desafio.Entities.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class User implements UserDetails {

    private Person usuario;

    public User(Person usuario) {
        this.usuario = usuario;
    }

    public Long getId(){
        return usuario.getId();
    }

    public String getName(){
        return usuario.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(usuario.role.name());
    }

    @Override
    public String getPassword() {
        return "{noop}" + usuario.password;
    }

    @Override
    public String getUsername() {
        return usuario.name;
    }
}

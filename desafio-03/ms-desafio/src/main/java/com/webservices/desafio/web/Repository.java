package com.webservices.desafio.web;

import com.webservices.desafio.Entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Repository extends JpaRepository <Person, Long> {
    Optional <Person> findByName(String username);
}

package com.webservices.desafio.dto;

import com.webservices.desafio.Entities.Person;

public class PersonRequest {
    public String name;
    public String password;
    public String email;
    public String cep;

    public static Person toModel(PersonRequest person){
        return new Person(person.name, person.password, person.email, person.cep);
    }
}

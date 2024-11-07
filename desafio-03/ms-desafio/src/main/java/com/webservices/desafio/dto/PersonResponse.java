package com.webservices.desafio.dto;

import com.webservices.desafio.Entities.Address;
import com.webservices.desafio.Entities.Person;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersonResponse {
    public String name;
    public String email;
    public Address address;

   public static PersonResponse toModel(Person person){
       return new PersonResponse(person.name, person.email, person.address);
    }
}

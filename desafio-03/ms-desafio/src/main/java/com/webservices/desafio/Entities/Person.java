package com.webservices.desafio.Entities;

import com.webservices.desafio.web.Repository;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Entity
@Data
@Table(name = "PERSON")
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column
    public String name;
    @Column
    public String password;
    @Column
    public String email;
    @Column
    public String cep;
    @Column
    public ROLE role = ROLE.COMMON_USER;

    public Address address;

    public Person(String name, String password, String email, String cep) {
        this.name = name;
        this.password = password;
        this.cep = cep;
        this.email = email;
    }

    public String fromJson(Person person){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: " + person.name + "\n");
        stringBuilder.append("Cep: " + person.cep + "\n");
        stringBuilder.append("E-mail: " + person.email);
        return stringBuilder.toString();
    }

}

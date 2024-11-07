package com.webservices.desafio.web;

import com.webservices.desafio.Auth.User;
import com.webservices.desafio.Entities.Person;
import com.webservices.desafio.Entities.ROLE;
import com.webservices.desafio.Exceptions.PasswordsNotMatchingException;
import com.webservices.desafio.Exceptions.PersonNotFoundException;
import com.webservices.desafio.ViaCepClient;
import com.webservices.desafio.dto.PersonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class Service {

    private final Repository repository;
    private final ViaCepClient viacep;

    // Metodo responsável por buscar uma pessoa pelo nome de usuário
    public Person findByUsername(String username){
        // Busca a pessoa pelo nome e, se não encontrar, lança uma exceção
        return repository.findByName(username).orElseThrow();
    }

    // Metodo responsável por salvar uma pessoa no banco de dados
    public void save(Person person) {
        // Obtém o endereço da pessoa com base no CEP utilizando o serviço ViaCepClient
        person.setAddress(viacep.getAddressByCep(person.cep));

        // Salva a pessoa no repositório
        repository.save(person);
    }

    // Metodo responsável por obter todas as pessoas e retornar uma lista de PersonResponse
    public List<PersonResponse> getAll() {
        // Busca todas as pessoas no repositório
        List<Person> list = repository.findAll();

        // Converte a lista de Person para uma lista de PersonResponse e retorna
        return list.stream().map(x -> PersonResponse.toModel(x)).collect(Collectors.toList());
    }

    // Metodo responsável por alterar a senha de uma pessoa
    public void changePassword(String password, String confirmPassword, Long id) {
        // Verifica se as senhas fornecidas são iguais, se não, lança uma exceção
        if(!password.equals(confirmPassword)){
            throw new PasswordsNotMatchingException("Passwords don`t match!");
        }
        else{
            // Busca a pessoa pelo ID, e se não encontrar, lança uma exceção
            Optional<Person> person = repository.findById(id);
            Person savePerson = person.orElseThrow(() -> new PersonNotFoundException("No matching ID"));

            // Altera a senha da pessoa e salva no repositório
            savePerson.setPassword(password);
            repository.save(savePerson);
        }
    }

    // Metodo responsável por buscar uma pessoa pelo ID
    public Person findById(Long id){
        // Busca a pessoa pelo ID, e se não encontrar, lança uma exceção
        var x = repository.findById(id);
        return x.orElseThrow(() -> new PersonNotFoundException("No matching ID"));
    }

    // Metodo responsável por inicializar dados de exemplo, chamado após a construção da classe
    @PostConstruct
    public void querys(){
        // Cria uma pessoa de exemplo
        Person person = new Person("Rafael", "12345", "@email.com", "11025021");

        // Define o papel da pessoa como ADMIN
        person.setRole(ROLE.ADMIN);

        // Salva a pessoa no banco de dados
        save(person);

        // Cria um usuário com base na pessoa (presumivelmente para outras finalidades)
        new User(person);
    }
}


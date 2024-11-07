package com.webservices.desafio.web;


import com.webservices.desafio.Entities.Person;
import com.webservices.desafio.ViaCepClient;
import com.webservices.desafio.dto.ChangePasswordRequest;
import com.webservices.desafio.dto.PersonRequest;
import com.webservices.desafio.dto.PersonResponse;
import com.webservices.desafio.queues.NotifylogSubscriber;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1")
public class Controller {

    private final Service service;
    private final NotifylogSubscriber notify;

    // Metodo responsável por registrar uma nova pessoa
    @PostMapping
    public ResponseEntity<String> registrarPessoa(@RequestBody PersonRequest person){
        // Converte a requisição para o modelo de Person
        Person pessoa = PersonRequest.toModel(person);

        // Salva a pessoa no banco de dados
        service.save(pessoa);

        // Converte a pessoa para um formato JSON para enviar uma mensagem
        String s = pessoa.fromJson(pessoa);

        // Envia uma mensagem de notificação indicando que um usuário foi registrado
        notify.sendMessage("\nUser Registered: \n" + s);

        // Retorna uma resposta HTTP 201 (Created) indicando que a pessoa foi registrada
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Metodo responsável por alterar a senha de um usuário
    @PutMapping
    public ResponseEntity changePassword(@RequestBody ChangePasswordRequest password){
        // Chama o serviço para alterar a senha do usuário
        service.changePassword(password.password, password.confirmPassword, password.id);

        // Envia uma mensagem de notificação indicando que a senha foi alterada
        notify.sendMessage("\nPassword changed from user: " + service.findById(password.id).name);

        // Retorna uma resposta HTTP 200 (OK) indicando que a senha foi alterada com sucesso
        return ResponseEntity.ok().build();
    }

    // Metodo responsável por recuperar todas as pessoas registradas
    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAll(){
        // Obtém a lista de todas as pessoas do banco de dados
        List<PersonResponse> list = service.getAll();

        // Retorna a lista de pessoas com uma resposta HTTP 200 (OK)
        return ResponseEntity.ok(list);
    }

    // Metodo responsável por recuperar uma pessoa pelo nome de usuário
    @GetMapping(params = "username")
    public ResponseEntity<PersonResponse> getUsername(@RequestParam String username){
        // Busca a pessoa pelo nome de usuário
        service.findByUsername(username);

        // Retorna uma resposta HTTP 200 (OK) após encontrar a pessoa
        return ResponseEntity.ok().build();
    }
}

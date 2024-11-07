package com.compass.notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotifyListener {

    // O metodo 'listenLog' é um listener do RabbitMQ, que escuta as mensagens da fila 'notifylog'
    @RabbitListener(queues = "${mq.queues.notifylog}")  // Anotação que define a fila do RabbitMQ para escutar as mensagens
    public void listenLog(@Payload String payload) {  // O parâmetro 'payload' contém a mensagem recebida da fila
        log.info(payload);  // Exibe a mensagem no log (informação recebida da fila)
    }
}


package com.webservices.desafio.queues;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class NotifylogSubscriber {

    @Value("${mq.queues.notifylog}")
    private String value;

    private final RabbitTemplate rabbitTemplate;
    private Queue queue;

    @PostConstruct
    public void init() {
        this.queue = new Queue(value, true);
    }

    public void sendMessage(String s) {
        rabbitTemplate.convertAndSend(value, s);
    }
}

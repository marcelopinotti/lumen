package com.marcelo.cartoes.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MqConfig {

    @Bean
    Queue queueEmissaoCartoes(
            @Value("${mq.queues.emissao-cartoes}") String nome,
            @Value("${mq.exchanges.emissao-cartoes-dlx}") String deadLetterExchange,
            @Value("${mq.queues.emissao-cartoes-dlq}") String deadLetterQueue) {
        return new Queue(nome, true, false, false, Map.of(
                "x-dead-letter-exchange", deadLetterExchange,
                "x-dead-letter-routing-key", deadLetterQueue));
    }

    @Bean
    DirectExchange emissaoCartoesDeadLetterExchange(
            @Value("${mq.exchanges.emissao-cartoes-dlx}") String nome) {
        return new DirectExchange(nome);
    }

    @Bean
    Queue queueEmissaoCartoesDlq(@Value("${mq.queues.emissao-cartoes-dlq}") String nome) {
        return new Queue(nome);
    }

    @Bean
    Binding emissaoCartoesDlqBinding(
            Queue queueEmissaoCartoesDlq,
            DirectExchange emissaoCartoesDeadLetterExchange) {
        return BindingBuilder.bind(queueEmissaoCartoesDlq)
                .to(emissaoCartoesDeadLetterExchange)
                .with(queueEmissaoCartoesDlq.getName());
    }
}

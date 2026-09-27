package com.marcelo.avaliador.infra.mqueue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcelo.avaliador.domain.EmissaoDeCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissaoCartoes;
    private final ObjectMapper objectMapper;

    public void solicitarEmissaoCartao(EmissaoDeCartao emissaoDeCartao) {
        try {
            CorrelationData correlation = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("", queueEmissaoCartoes.getName(), objectMapper.writeValueAsString(emissaoDeCartao), correlation);
            CorrelationData.Confirm confirm = correlation.getFuture().get(5, TimeUnit.SECONDS);
            if (!confirm.ack() || correlation.getReturned() != null) {
                throw new IllegalStateException("RabbitMQ não confirmou a mensagem: " + confirm.reason());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Publicação interrompida", e);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao enviar mensagem para a fila", e);
        }
    }
}

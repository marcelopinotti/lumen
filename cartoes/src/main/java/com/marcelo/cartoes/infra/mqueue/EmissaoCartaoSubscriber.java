package com.marcelo.cartoes.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcelo.cartoes.application.ClienteCartaoService;
import com.marcelo.cartoes.domain.SolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmissaoCartaoSubscriber {

    private final ObjectMapper objectMapper;
    private final ClienteCartaoService clienteCartaoService;

    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissaoCartao(@Payload String payload) {
        SolicitacaoEmissaoCartao solicitacao;
        try {
            solicitacao = objectMapper.readValue(payload, SolicitacaoEmissaoCartao.class);
        } catch (JsonProcessingException e) {
            throw new AmqpRejectAndDontRequeueException("Mensagem de emissão de cartão inválida", e);
        }

        if (solicitacao.cartao() == null || solicitacao.cpf() == null || solicitacao.cpf().isBlank()
                || solicitacao.enderecoDeEntrega() == null || solicitacao.enderecoDeEntrega().isBlank()
                || solicitacao.limiteLiberado() == null || solicitacao.limiteLiberado().signum() <= 0) {
            throw new AmqpRejectAndDontRequeueException("Mensagem de emissão de cartão inválida");
        }

        try {
            clienteCartaoService.associar(solicitacao.cpf(), solicitacao.cartao(), solicitacao.limiteLiberado(), solicitacao.enderecoDeEntrega());
            log.info("Solicitação de emissão do cartão {} para o CPF {} recebida", solicitacao.cartao(), solicitacao.cpf());
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() != HttpStatus.CONFLICT) throw e;
            log.info("Solicitação de emissão já processada para o cartão {} e CPF {}", solicitacao.cartao(), solicitacao.cpf());
        } catch (DataIntegrityViolationException e) {
            if (!clienteCartaoService.jaAssociado(solicitacao.cpf(), solicitacao.cartao())) throw e;
            log.info("Solicitação de emissão já processada para o cartão {} e CPF {}", solicitacao.cartao(), solicitacao.cpf());
        }
    }

}

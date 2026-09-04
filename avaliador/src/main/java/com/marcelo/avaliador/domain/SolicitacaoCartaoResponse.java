package com.marcelo.avaliador.domain;
import java.math.BigDecimal;
public record SolicitacaoCartaoResponse(String cpf, String nome, Long cartaoId, String cartao, String categoria, BigDecimal limiteAprovado) {}

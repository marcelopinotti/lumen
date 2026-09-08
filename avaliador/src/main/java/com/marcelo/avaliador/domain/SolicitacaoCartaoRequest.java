package com.marcelo.avaliador.domain;

import java.math.BigDecimal;

public record SolicitacaoCartaoRequest(String cpf, BigDecimal rendaMensal, Long cartaoId, String enderecoDeEntrega) {
}

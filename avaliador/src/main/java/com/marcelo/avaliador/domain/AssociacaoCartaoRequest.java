package com.marcelo.avaliador.domain;
import java.math.BigDecimal;
public record AssociacaoCartaoRequest(String cpf, Long cartaoId, BigDecimal limite) {}

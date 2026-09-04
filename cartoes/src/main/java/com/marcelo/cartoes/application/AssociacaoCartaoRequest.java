package com.marcelo.cartoes.application;
import java.math.BigDecimal;
public record AssociacaoCartaoRequest(String cpf, Long cartaoId, BigDecimal limite) {}

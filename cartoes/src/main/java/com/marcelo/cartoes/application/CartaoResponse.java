package com.marcelo.cartoes.application;

import com.marcelo.cartoes.domain.Bandeira;
import com.marcelo.cartoes.domain.CorCartao;

import java.math.BigDecimal;

public record CartaoResponse(Long id, String nome, Bandeira bandeira, BigDecimal renda, CorCartao cor, BigDecimal limiteBasico) {
}
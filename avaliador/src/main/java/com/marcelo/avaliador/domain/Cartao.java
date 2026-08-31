package com.marcelo.avaliador.domain;

import java.math.BigDecimal;

public record Cartao(
        Long id,
        String nome,
        String bandeira,
        BigDecimal renda,
        String cor,
        BigDecimal limiteBasico
) {
}

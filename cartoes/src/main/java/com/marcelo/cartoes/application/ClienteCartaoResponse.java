package com.marcelo.cartoes.application;

import java.math.BigDecimal;
public record ClienteCartaoResponse(
        String nome,
        String bandeira,
        BigDecimal limiteLiberado) {
}

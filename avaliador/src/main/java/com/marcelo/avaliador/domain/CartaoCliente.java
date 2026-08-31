package com.marcelo.avaliador.domain;

import java.math.BigDecimal;

public record CartaoCliente(
        String nome,
        String bandeira,
        BigDecimal limiteLiberado
) {

}


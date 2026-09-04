package com.marcelo.avaliador.domain;

import java.math.BigDecimal;

public record RetornoAvaliacao(
    String cpf,
    String nome,
    BigDecimal valorAprovado,
    String mensagem
) {}

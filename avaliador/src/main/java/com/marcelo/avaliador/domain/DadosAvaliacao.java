package com.marcelo.avaliador.domain;

import java.math.BigDecimal;

public record DadosAvaliacao(
    String cpf,
    String nome,
    int idade,
    Long renda,
    BigDecimal limiteBasico
) {}

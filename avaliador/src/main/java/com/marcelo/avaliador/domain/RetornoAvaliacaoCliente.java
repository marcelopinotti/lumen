package com.marcelo.avaliador.domain;

import java.math.BigDecimal;
import java.util.List;

public record RetornoAvaliacaoCliente(
    String cpf,
    String nome,
    List<CartaoAprovado> cartoesAprovados
) {
    public record CartaoAprovado(
        String cartao,
        String bandeira,
        BigDecimal limiteAprovado
    ) {}
}

package com.marcelo.cartoes.domain;

import java.math.BigDecimal;

public record SolicitacaoEmissaoCartao(
        Long cartao,
        String cpf,
        String enderecoDeEntrega,
        BigDecimal limiteLiberado
) {
}

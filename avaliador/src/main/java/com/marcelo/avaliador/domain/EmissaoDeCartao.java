package com.marcelo.avaliador.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public record EmissaoDeCartao(Long cartao, String cpf, String enderecoDeEntrega,
                              BigDecimal limiteLiberado) implements Serializable {
}

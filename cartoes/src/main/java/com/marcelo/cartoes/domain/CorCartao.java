package com.marcelo.cartoes.domain;

import java.math.BigDecimal;

public enum CorCartao {
    PRATA("Prata", "0"),
    OURO("Ouro", "5000"),
    PLATINA("Platina", "10000"),
    BLACK("Black", "20000");
    private final String nome;
    private final BigDecimal rendaMinima;
    CorCartao(String nome, String rendaMinima) { this.nome = nome; this.rendaMinima = new BigDecimal(rendaMinima); }
    public BigDecimal rendaMinima() { return rendaMinima; }
}

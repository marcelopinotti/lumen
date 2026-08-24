package com.marcelo.cartoes.domain;

public enum CorCartao {
    PRATA("Prata"),
    OURO("Ouro"),
    PLATINA("Platina"),
    BLACK("Black");

    private String nome;

    CorCartao(String nome) {
        this.nome = nome;
    }
}
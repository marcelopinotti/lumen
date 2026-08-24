package com.marcelo.cartoes.domain;

public enum Bandeira {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    ELO("Elo"),
    AMERICAN_EXPRESS("American Express");


    private String nome;

    Bandeira(String nome) {
        this.nome = nome;
    }

}

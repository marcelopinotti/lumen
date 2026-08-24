package com.marcelo.cartoes.application;

public record ClienteCartaoResponse(
        String nome,
        String bandeira,
        String limite) {
}

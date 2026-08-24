package com.marcelo.cartoes.domain;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Bandeira bandeira;

    private BigDecimal renda;

    @Enumerated(EnumType.STRING)
    private CorCartao cor;

    private BigDecimal limiteBasico;


    public Cartao(String nome, Bandeira bandeira, BigDecimal renda, CorCartao cor) {
        this.nome = nome;
        this.bandeira = bandeira;
        this.renda = renda;
        this.cor = cor;
    }
}

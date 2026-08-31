package com.marcelo.avaliador.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartaoCliente {

    private String nome;

    private String bandeira;

    private BigDecimal limiteLiberado;

}

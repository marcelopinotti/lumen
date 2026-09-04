package com.marcelo.avaliador.domain;

import lombok.Builder;
import java.util.List;

@Builder
public record SituacaoCliente(
    DadosCliente cliente,
    List<CartaoCliente> cartoes
) {}

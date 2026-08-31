package com.marcelo.avaliador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record CpfApiResponse(
        Integer code,
        DadosCpfApi data) {
}

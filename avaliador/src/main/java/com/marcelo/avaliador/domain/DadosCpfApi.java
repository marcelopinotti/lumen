package com.marcelo.avaliador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosCpfApi(
        String cpf,
        String nome,
        String genero,
        @JsonProperty("data_nascimento") String dataNascimento) {
}

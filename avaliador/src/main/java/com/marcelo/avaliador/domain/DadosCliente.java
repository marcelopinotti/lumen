package com.marcelo.avaliador.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DadosCliente(
        String cpf,
        String nome,
        Integer idade,
        String genero,
        String dataNascimento) {

    /** Construtor usado pelo Feign (clientes do banco local - sem genero/dataNascimento) */
    public DadosCliente(String cpf, String nome, Integer idade) {
        this(cpf, nome, idade, null, null);
    }
}

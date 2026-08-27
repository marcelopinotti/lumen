package com.marcelo.avaliador.application;

import com.marcelo.avaliador.domain.DadosCliente;
import com.marcelo.avaliador.domain.SituacaoCliente;
import com.marcelo.avaliador.infra.clientes.ClienteControllerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AvaliadorService {


    private final ClienteControllerClient clienteControllerClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) {
        DadosCliente dadosCliente = clienteControllerClient.dadosCliente(cpf);

        return SituacaoCliente.builder()
                .cliente(dadosCliente)
                .build();
    }
}

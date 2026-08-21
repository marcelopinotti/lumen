package com.marcelo.lumen.clientes.application;

import com.marcelo.lumen.clientes.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getCpf(), cliente.getNome(),cliente.getIdade());
    }

    public Cliente toModel(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setCpf(request.cpf());
        cliente.setNome(request.nome());
        cliente.setIdade(request.idade());
        return cliente;
    }
}



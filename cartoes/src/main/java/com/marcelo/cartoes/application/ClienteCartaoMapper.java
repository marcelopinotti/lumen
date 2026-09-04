package com.marcelo.cartoes.application;

import com.marcelo.cartoes.domain.ClienteCartao;
import org.springframework.stereotype.Component;

@Component
public class ClienteCartaoMapper {

    public ClienteCartaoResponse toResponse(ClienteCartao clienteCartao) {
        return new ClienteCartaoResponse(
                clienteCartao.getCartao().getNome(),
                clienteCartao.getCartao().getBandeira().toString(),
                clienteCartao.getLimite()
        );
    }
}

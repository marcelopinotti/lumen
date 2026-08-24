package com.marcelo.cartoes.application;

import com.marcelo.cartoes.domain.Cartao;
import org.springframework.stereotype.Component;

@Component
public class CartaoMapper {

    public CartaoResponse toResponse(Cartao cartao) {
        return new CartaoResponse(
                cartao.getId(),
                cartao.getNome(),
                cartao.getBandeira(),
                cartao.getRenda(),
                cartao.getCor(),
                cartao.getLimiteBasico()
        );
    }

    public Cartao toModel(CartaoRequest request) {
        Cartao cartao = new Cartao();
        cartao.setNome(request.nome());
        cartao.setBandeira(request.bandeira());
        cartao.setRenda(request.renda());
        cartao.setCor(request.cor());
        cartao.setLimiteBasico(request.limiteBasico());
        return cartao;
    }
}
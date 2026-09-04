package com.marcelo.cartoes.application;

import com.marcelo.cartoes.domain.Cartao;
import com.marcelo.cartoes.infra.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository cartaoRepository;


    @Transactional
    public Cartao save(Cartao cartao) {
        if (cartao.getRenda() == null || cartao.getCor() == null || cartao.getRenda().compareTo(cartao.getCor().rendaMinima()) < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A renda mínima do cartão não atende a categoria selecionada");
        if (cartao.getLimiteBasico() == null || cartao.getLimiteBasico().signum() <= 0 || cartao.getLimiteBasico().compareTo(cartao.getCor().rendaMinima()) < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O limite básico não atende a categoria selecionada");
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> renda(Long renda) {
        var rendaBigDecimal = BigDecimal.valueOf(renda);
        return cartaoRepository.findByRendaLessThanEqual(rendaBigDecimal);
    }

    public Cartao buscarPorId(Long id) {
        return cartaoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não localizado"));
    }

}

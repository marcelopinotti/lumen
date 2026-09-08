package com.marcelo.cartoes.application;

import com.marcelo.cartoes.domain.ClienteCartao;
import com.marcelo.cartoes.domain.Cartao;
import com.marcelo.cartoes.infra.repository.CartaoRepository;
import com.marcelo.cartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository clienteCartaoRepository;
    private final CartaoRepository cartaoRepository;

    public List<ClienteCartao> findByCpf(String cpf) {
        return clienteCartaoRepository.findByCpf(cpf);
    }

    public boolean jaAssociado(String cpf, Long cartaoId) {
        return clienteCartaoRepository.existsByCpfAndCartaoId(cpf, cartaoId);
    }

    @Transactional
    public ClienteCartao associar(String cpf, Long cartaoId, BigDecimal limite, String enderecoDeEntrega) {
        if (clienteCartaoRepository.existsByCpfAndCartaoId(cpf, cartaoId)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Cartão já associado a este CPF");
        Cartao cartao = cartaoRepository.findById(cartaoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não localizado"));
        ClienteCartao associacao = new ClienteCartao();
        associacao.setCpf(cpf); associacao.setCartao(cartao); associacao.setLimite(limite); associacao.setEnderecoDeEntrega(enderecoDeEntrega);
        return clienteCartaoRepository.save(associacao);
    }

}

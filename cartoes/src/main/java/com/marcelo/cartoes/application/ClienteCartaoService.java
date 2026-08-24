package com.marcelo.cartoes.application;

import com.marcelo.cartoes.domain.ClienteCartao;
import com.marcelo.cartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository clienteCartaoRepository;

    public List<ClienteCartao> findByCpf(String cpf) {
        return clienteCartaoRepository.findByCpf(cpf);
    }

}

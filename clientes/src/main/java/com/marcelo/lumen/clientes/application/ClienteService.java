package com.marcelo.lumen.clientes.application;

import com.marcelo.lumen.clientes.domain.Cliente;
import com.marcelo.lumen.clientes.infra.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper clienteMapper;

    @Transactional
    public ClienteResponse save(ClienteRequest request) {
        Cliente savedCliente = repository.save(clienteMapper.toModel(request));
        return clienteMapper.toResponse(savedCliente);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }


}

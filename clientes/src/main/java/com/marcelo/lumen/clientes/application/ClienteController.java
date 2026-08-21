package com.marcelo.lumen.clientes.application;

import com.marcelo.lumen.clientes.domain.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/clientes")
@RequiredArgsConstructor
public class ClienteController {


    private final ClienteService service;



    @PostMapping
    public ResponseEntity<ClienteResponse> save(@RequestBody ClienteRequest request) {
        ClienteResponse response = service.save(request);
        URI headerLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{cpf}")
                .buildAndExpand(response.cpf())
                .toUri();
        return ResponseEntity.created(headerLocation).body(response);
    }

    @GetMapping
    public ResponseEntity<Object> dadosCliente(@RequestParam("cpf") String cpf) {
        Optional<Cliente> cliente = service.findByCpf(cpf);
        if (cliente.isPresent()) {
            Cliente value = cliente.get();
            return ResponseEntity.ok(new ClienteResponse(value.getNome(), value.getCpf(), value.getIdade()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", "CPF não localizado"));
    }
}

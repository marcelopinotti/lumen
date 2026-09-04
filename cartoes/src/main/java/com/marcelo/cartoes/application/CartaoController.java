package com.marcelo.cartoes.application;

import com.marcelo.cartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/cartoes")
public class CartaoController {

    private final CartaoService cartaoService;
    private final CartaoMapper cartaoMapper;
    private final ClienteCartaoService clienteCartaoService;
    private final ClienteCartaoMapper clienteCartaoMapper;

    @PostMapping
    public ResponseEntity<CartaoResponse> save(@RequestBody CartaoRequest request) {
        var cartao = cartaoMapper.toModel(request);
        var savedCartao = cartaoService.save(cartao);
        var response = cartaoMapper.toResponse(savedCartao);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaoResponse> buscarPorId(@PathVariable Long id) { return ResponseEntity.ok(cartaoMapper.toResponse(cartaoService.buscarPorId(id))); }

    @PostMapping("/associacoes")
    public ResponseEntity<ClienteCartaoResponse> associar(@RequestBody AssociacaoCartaoRequest request) {
        return ResponseEntity.status(201).body(clienteCartaoMapper.toResponse(clienteCartaoService.associar(request.cpf(), request.cartaoId(), request.limite())));
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<CartaoResponse>> renda(@RequestParam("renda") Long renda) {
        var cartoes = cartaoService.renda(renda);
        var response = cartoes.stream()
                .map(cartaoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<ClienteCartaoResponse>> getdByCpf(@RequestParam("cpf") String cpf) {
        List<ClienteCartao> lista = clienteCartaoService.findByCpf(cpf);
        List<ClienteCartaoResponse> response = lista.stream()
                .map(clienteCartaoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}

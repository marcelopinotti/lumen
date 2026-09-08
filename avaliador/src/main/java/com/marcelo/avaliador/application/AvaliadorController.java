package com.marcelo.avaliador.application;

import com.marcelo.avaliador.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avaliador")
@RequiredArgsConstructor
public class AvaliadorController {

    private final AvaliadorService service;

    @PostMapping
    public ResponseEntity<RetornoAvaliacao> realizarAvaliacao(@RequestBody DadosAvaliacao dados) {
        var resultado = service.realizarAvaliacao(dados);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/solicitacoes")
    public ResponseEntity<SolicitacaoCartaoResponse> solicitarCartao(@RequestBody SolicitacaoCartaoRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.solicitarCartao(request));
    }

    @GetMapping(value = "/situacao", params = "cpf")
    public ResponseEntity<SituacaoCliente> SituacaoCliente(@RequestParam("cpf") String cpf) {
        SituacaoCliente response = service.obterSituacaoCliente(cpf);
        return ResponseEntity.ok(response);

    }
}

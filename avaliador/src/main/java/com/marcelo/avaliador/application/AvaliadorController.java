package com.marcelo.avaliador.application;

import com.marcelo.avaliador.domain.DadosAvaliacao;
import com.marcelo.avaliador.domain.RetornoAvaliacao;
import com.marcelo.avaliador.domain.SituacaoCliente;
import lombok.RequiredArgsConstructor;
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

    @GetMapping(value = "/situacao",params = "cpf")
    public ResponseEntity<SituacaoCliente> SituacaoCliente(@RequestParam("cpf") String cpf) {
        SituacaoCliente response = service.obterSituacaoCliente(cpf);
        return ResponseEntity.ok(response);

    }
}

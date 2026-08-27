package com.marcelo.avaliador.application;

import com.marcelo.avaliador.domain.SituacaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/avaliador")
@RequiredArgsConstructor
public class AvaliadorController {

    private final AvaliadorService service;



    @GetMapping
    public String ok() {
        return "ok";
    }

    @GetMapping(value = "/situacao",params = "cpf")
    public ResponseEntity<SituacaoCliente> SituacaoCliente(@RequestParam("cpf") String cpf) {
        SituacaoCliente response = service.obterSituacaoCliente(cpf);
        return ResponseEntity.ok(response);

        //TODO: arrumar porque o feign client não está funcionando, não está subindo
    }
}

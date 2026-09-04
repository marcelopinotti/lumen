package com.marcelo.avaliador.infra.clientes;

import com.marcelo.avaliador.domain.Cartao;
import com.marcelo.avaliador.domain.CartaoCliente;
import com.marcelo.avaliador.domain.AssociacaoCartaoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(path = "/api/cartoes", value = "cartoes")
public interface CartoesControllerClient {

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartaoCliente>> getdByCpf(@RequestParam("cpf") String cpf);

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda);

    @GetMapping("/{id}")
    Cartao buscarPorId(@PathVariable Long id);

    @PostMapping("/associacoes")
    void associar(@RequestBody AssociacaoCartaoRequest request);
}

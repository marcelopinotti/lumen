package com.marcelo.avaliador.infra.clientes;

import com.marcelo.avaliador.domain.DadosCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(path = "/api/clientes", value = "clientes") // url que o spring vai resolver o nome do serviço no eureka
public interface ClienteControllerClient {
    @GetMapping
    DadosCliente dadosCliente(@RequestParam("cpf") String cpf);
}

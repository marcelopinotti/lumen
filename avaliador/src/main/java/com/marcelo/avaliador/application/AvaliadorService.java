package com.marcelo.avaliador.application;

import com.marcelo.avaliador.domain.*;
import com.marcelo.avaliador.infra.clientes.CartoesControllerClient;
import com.marcelo.avaliador.infra.clientes.ClienteControllerClient;
import com.marcelo.avaliador.infra.cpfapi.CpfApiClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvaliadorService {

    private final ClienteControllerClient clienteControllerClient;
    private final CartoesControllerClient cartoesControllerClient;
    private final CpfApiClient cpfApiClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) {

        DadosCliente dadosCliente = buscarClienteLocal(cpf);

        DadosCpfApi dadosCpf = cpfApiClient.consultarCpf(cpf).orElse(null);

        if (dadosCliente == null) {
            if (dadosCpf == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "CPF não localizado no sistema nem na API externa");
            }
            log.info("CPF {} não está no banco local. Usando dados da API externa.", cpf);
            dadosCliente = new DadosCliente(
                    dadosCpf.cpf(),
                    dadosCpf.nome(),
                    calcularIdade(dadosCpf.dataNascimento()),
                    dadosCpf.genero(),
                    dadosCpf.dataNascimento()
            );
        } else if (dadosCpf != null) {
            Integer idade = dadosCliente.idade() != null
                    ? dadosCliente.idade()
                    : calcularIdade(dadosCpf.dataNascimento());

            dadosCliente = new DadosCliente(
                    dadosCliente.cpf(),
                    dadosCliente.nome(),
                    idade,
                    dadosCpf.genero(),
                    dadosCpf.dataNascimento()
            );
        }
        List<CartaoCliente> cartoes = buscarCartoes(cpf);

        return SituacaoCliente.builder()
                .cliente(dadosCliente)
                .cartoes(cartoes)
                .build();
    }


    private Integer calcularIdade(String dataNascimento) {
        if (dataNascimento == null || dataNascimento.isBlank()) return null;
        try {
            LocalDate nascimento = LocalDate.parse(dataNascimento);
            return Period.between(nascimento, LocalDate.now()).getYears();
        } catch (Exception e) {
            log.warn("Não foi possível calcular a idade a partir da data: {}", dataNascimento);
            return null;
        }
    }


    private DadosCliente buscarClienteLocal(String cpf) {
        try {
            return clienteControllerClient.dadosCliente(cpf);
        } catch (FeignException.NotFound e) {
            log.info("CPF {} não encontrado no banco local.", cpf);
            return null;
        }
    }


    private List<CartaoCliente> buscarCartoes(String cpf) {
        try {
            var response = cartoesControllerClient.getdByCpf(cpf);
            return response.getBody() != null ? response.getBody() : List.of();
        } catch (FeignException e) {
            log.warn("Erro ao buscar cartões para CPF {}: {}", cpf, e.getMessage());
            return List.of();
        }
    }

    public RetornoAvaliacao realizarAvaliacao(DadosAvaliacao dados) {

        int idade = dados.idade();
        BigDecimal limiteBasico = dados.limiteBasico();

        BigDecimal fatorBonusPorAno = new BigDecimal("150.00");

        int anosExperiencia = Math.max(0, idade - 18);

        BigDecimal valorBonusIdade = fatorBonusPorAno.multiply(new BigDecimal(anosExperiencia));

        BigDecimal limiteAprovado = limiteBasico.add(valorBonusIdade);

       return new RetornoAvaliacao(
                dados.cpf(),
                dados.nome(),
                limiteAprovado,
                "Cartão aprovado com sucesso!"
        );
    }
}
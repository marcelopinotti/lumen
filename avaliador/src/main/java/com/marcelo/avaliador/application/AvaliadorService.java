package com.marcelo.avaliador.application;

import com.marcelo.avaliador.domain.*;
import com.marcelo.avaliador.infra.clientes.CartoesControllerClient;
import com.marcelo.avaliador.infra.clientes.ClienteControllerClient;
import com.marcelo.avaliador.infra.cpfapi.CpfApiClient;
import com.marcelo.avaliador.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvaliadorService {

    private final ClienteControllerClient clienteControllerClient;
    private final CartoesControllerClient cartoesControllerClient;
    private final CpfApiClient cpfApiClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

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
            return Period.between(nascimento, LocalDate.now(ZoneId.of("America/Sao_Paulo"))).getYears();
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
        } catch (FeignException e) {
            log.warn("Erro ao buscar cliente para CPF {}: {}", cpf, e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível consultar o cliente");
        }
    }


    private List<CartaoCliente> buscarCartoes(String cpf) {
        try {
            var response = cartoesControllerClient.getdByCpf(cpf);
            return response.getBody() != null ? response.getBody() : List.of();
        } catch (FeignException e) {
            log.warn("Erro ao buscar cartões para CPF {}: {}", cpf, e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível consultar os cartões do cliente");
        }
    }

    public RetornoAvaliacao realizarAvaliacao(DadosAvaliacao dados) {
        validarMaioridade(dados.idade());
        BigDecimal limiteAprovado = calcularLimite(dados.renda() == null ? null : BigDecimal.valueOf(dados.renda()), dados.limiteBasico());

        return new RetornoAvaliacao(
                dados.cpf(),
                dados.nome(),
                limiteAprovado,
                "Cartão aprovado com sucesso!"
        );
    }

    public SolicitacaoCartaoResponse solicitarCartao(SolicitacaoCartaoRequest request) {
        String cpf = normalizarCpf(request.cpf());
        validarRenda(request.rendaMensal());
        if (request.cartaoId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O cartão é obrigatório");
        if (request.enderecoDeEntrega() == null || request.enderecoDeEntrega().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O endereço de entrega é obrigatório");
        DadosCliente cliente = buscarOuCadastrarCliente(cpf);
        validarMaioridade(cliente.idade());
        Cartao cartao = buscarCartao(request.cartaoId());
        if (cartao.renda() == null || request.rendaMensal().compareTo(cartao.renda()) < 0)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "A renda declarada não atende ao cartão solicitado");
        BigDecimal limite = calcularLimite(request.rendaMensal(), cartao.limiteBasico());
        try {
            emissaoCartaoPublisher.solicitarEmissaoCartao(
                    new EmissaoDeCartao(cartao.id(), cpf, request.enderecoDeEntrega(), limite));
        } catch (Exception e) {
            log.error("Erro ao publicar solicitação de emissão de cartão", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível solicitar a emissão do cartão");
        }
        return new SolicitacaoCartaoResponse(cpf, cliente.nome(), cartao.id(), cartao.nome(), cartao.cor(), limite);
    }

    private DadosCliente buscarOuCadastrarCliente(String cpf) {
        DadosCliente cliente = buscarClienteLocal(cpf);
        if (cliente != null) return cliente;
        DadosCpfApi externo = cpfApiClient.consultarCpf(cpf)
                .filter(dados -> cpf.equals(normalizarCpf(dados.cpf())))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CPF não localizado na API externa"));
        Integer idade = calcularIdade(externo.dataNascimento());
        if (idade == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "A API externa não informou uma data de nascimento válida");
        return clienteControllerClient.cadastrar(new ClienteCadastroRequest(cpf, externo.nome(), idade));
    }

    private Cartao buscarCartao(Long id) {
        try {
            return cartoesControllerClient.buscarPorId(id);
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não localizado");
        } catch (FeignException e) {
            log.warn("Erro ao buscar cartão {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível consultar o cartão");
        }
    }

    private void validarMaioridade(Integer idade) {
        if (idade == null || idade < 18)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "É necessário ter pelo menos 18 anos");
    }

    private void validarRenda(BigDecimal renda) {
        if (renda == null || renda.signum() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A renda mensal deve ser maior que zero");
    }

    private BigDecimal calcularLimite(BigDecimal renda, BigDecimal limiteBasico) {
        validarRenda(renda);
        if (limiteBasico == null || limiteBasico.signum() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O limite básico deve ser maior que zero");
        return limiteBasico.min(renda);
    }

    private String normalizarCpf(String cpf) {
        String normalizado = cpf == null ? "" : cpf.replaceAll("\\D", "");
        if (!normalizado.matches("\\d{11}"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF deve conter 11 dígitos");
        return normalizado;
    }
}

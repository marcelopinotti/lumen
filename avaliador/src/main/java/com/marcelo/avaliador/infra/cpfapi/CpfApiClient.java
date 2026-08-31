package com.marcelo.avaliador.infra.cpfapi;

import com.marcelo.avaliador.domain.CpfApiResponse;
import com.marcelo.avaliador.domain.DadosCpfApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class CpfApiClient {

    private static final String BASE_URL = "https://api-cpf-gratis.p.rapidapi.com/?cpf=";
    private static final String RAPIDAPI_HOST = "api-cpf-gratis.p.rapidapi.com";

    private final RestTemplate restTemplate;

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    public Optional<DadosCpfApi> consultarCpf(String cpf) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-rapidapi-key", rapidApiKey);
            headers.set("x-rapidapi-host", RAPIDAPI_HOST);
            headers.set("Content-Type", "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<CpfApiResponse> response = restTemplate.exchange(
                    BASE_URL + cpf,
                    HttpMethod.GET,
                    entity,
                    CpfApiResponse.class
            );

            // Extrai o objeto "data" de dentro da resposta
            return Optional.ofNullable(response.getBody())
                    .map(CpfApiResponse::data);

        } catch (Exception e) {
            log.warn("Erro ao consultar CPF {} na API externa: {}", cpf, e.getMessage());
            return Optional.empty();
        }
    }
}

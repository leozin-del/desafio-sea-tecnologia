package com.seatecnologia.client_api.client;

import com.seatecnologia.client_api.DTO.ViaCepResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CepClient {

    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";

    private final RestTemplate restTemplate;

    public CepClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ViaCepResponseDTO buscarPorCep(String cep) {
        ViaCepResponseDTO resposta = restTemplate.getForObject(VIA_CEP_URL, ViaCepResponseDTO.class, cep);

        if (resposta == null || Boolean.TRUE.equals(resposta.erro())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP não encontrado: " + cep);
        }

        return resposta;
    }
}

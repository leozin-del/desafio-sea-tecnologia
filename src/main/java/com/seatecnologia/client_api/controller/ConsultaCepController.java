package com.seatecnologia.client_api.controller;

import com.seatecnologia.client_api.DTO.ViaCepResponseDTO;
import com.seatecnologia.client_api.client.CepClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsultaCepController {

    private final CepClient cepClient;

    public ConsultaCepController(CepClient cepClient) {
        this.cepClient = cepClient;
    }

    @GetMapping("/cep/{cep}")
    public ViaCepResponseDTO consultarCep(@PathVariable String cep) {
        return cepClient.buscarPorCep(cep);
    }
}

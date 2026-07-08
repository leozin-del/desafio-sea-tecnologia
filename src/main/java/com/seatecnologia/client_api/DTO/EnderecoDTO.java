package com.seatecnologia.client_api.DTO;

import jakarta.validation.constraints.NotBlank;

public record EnderecoDTO(
        @NotBlank
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String uf,
        String complemento
)
{}

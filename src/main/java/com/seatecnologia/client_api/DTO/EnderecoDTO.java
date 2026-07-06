package com.seatecnologia.client_api.DTO;

import jakarta.validation.constraints.NotBlank;

public record EnderecoDTO(
        @NotBlank
        String cep,
        @NotBlank
        String logradouro,
        @NotBlank
        String bairro,
        @NotBlank
        String cidade,
        @NotBlank
        String uf,
        String complemento
)
{}

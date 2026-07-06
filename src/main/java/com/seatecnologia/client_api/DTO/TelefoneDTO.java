package com.seatecnologia.client_api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TelefoneDTO(
        @NotBlank
        @Pattern(regexp = "CELULAR|RESIDENCIAL|COMERCIAL", message = "Tipo deve ser CELULAR, RESIDENCIAL ou COMERCIAL")
        String tipo,
        @NotBlank String numero
)
{}

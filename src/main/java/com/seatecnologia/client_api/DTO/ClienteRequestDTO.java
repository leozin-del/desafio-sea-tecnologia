package com.seatecnologia.client_api.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ClienteRequestDTO(
        @NotBlank @Size(min = 3, max = 100)
        @Pattern(regexp = "^[\\p{L}\\p{N} ]+$", message = "Nome deve conter apenas letras, espaços e números")
        String nome,
        @NotBlank
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}", message = "CPF deve estar no formato 999.999.999-99 ou conter 11 dígitos")
        String cpf,
        @NotEmpty List<@Valid TelefoneDTO> telefones,
        @NotEmpty List<@Email String> emails,
        @NotNull @Valid EnderecoDTO endereco
)
{}

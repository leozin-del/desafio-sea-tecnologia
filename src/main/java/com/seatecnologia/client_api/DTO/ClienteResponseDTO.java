package com.seatecnologia.client_api.DTO;

import java.util.List;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        List<TelefoneDTO> telefones,
        List<String> emails,
        EnderecoDTO endereco
)
{}

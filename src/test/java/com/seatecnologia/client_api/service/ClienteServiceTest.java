package com.seatecnologia.client_api.service;

import com.seatecnologia.client_api.DTO.ClienteRequestDTO;
import com.seatecnologia.client_api.DTO.EnderecoDTO;
import com.seatecnologia.client_api.DTO.TelefoneDTO;
import com.seatecnologia.client_api.client.CepClient;
import com.seatecnologia.client_api.entity.Cliente;
import com.seatecnologia.client_api.entity.ClienteEndereco;
import com.seatecnologia.client_api.exception.CpfDuplicadoException;
import com.seatecnologia.client_api.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private CepClient cepClient;
    @InjectMocks
    private ClienteService clienteService;

    @Captor
    private ArgumentCaptor<Cliente> argumentCaptor;


    @Nested
    class criarNovoCliente {
        @Test
        @DisplayName("DeveCriarClienteComSucesso")
        void deveCriarClienteComSucesso() {
            var telefone = new TelefoneDTO("CELULAR", "61999999999");
            var endereco = new EnderecoDTO(
                    "70712900",
                    "SCN Quadra 2",
                    "Asa Norte",
                    "Brasília",
                    "DF",
                    null);
            var requestInput = new ClienteRequestDTO(
                    "Leonardo",
                    "066.639.871-27",
                    List.of(telefone),
                    List.of("leo@email.com"),
                    endereco
            );
            var clienteEsperado = new Cliente();
            clienteEsperado.setNome("Leonardo");
            clienteEsperado.setCpf("06663987127");
            clienteEsperado.setTelefones(List.of());
            clienteEsperado.setEmails(List.of());

            clienteEsperado.setEndereco(new ClienteEndereco(
                    null, clienteEsperado, "70712900", "SCN Quadra 2", "Asa Norte", "Brasília", "DF", null));


            when(clienteRepository.existsByCpf(any())).thenReturn(false);
            when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteEsperado);

            var clienteCriado = clienteService.criarCliente(requestInput);
            verify(clienteRepository).save(argumentCaptor.capture());
            var clienteCapturado = argumentCaptor.getValue();

            assertNotNull(clienteCriado);
            assertEquals("Leonardo", clienteCapturado.getNome());
            assertEquals("06663987127", clienteCapturado.getCpf());
        }

        @Test
        @DisplayName("DeveLancarExcecaoQuandoCpfJaCadastrado")
        void deveLancarExcecaoQuandoCpfJaCadastrado() {
            var telefone = new TelefoneDTO("CELULAR", "61999999999");
            var endereco = new EnderecoDTO(
                    "70712900",
                    "SCN Quadra 2",
                    "Asa Norte",
                    "Brasília",
                    "DF",
                    null);
            var requestInput = new ClienteRequestDTO(
                    "Leonardo",
                    "066.639.871-27",
                    List.of(telefone),
                    List.of("leo@email.com"),
                    endereco
            );

            when(clienteRepository.existsByCpf(any())).thenReturn(true);

            assertThrows(CpfDuplicadoException.class,
                    () -> clienteService.criarCliente(requestInput));

            verify(clienteRepository, never()).save(any());
        }
    }



}
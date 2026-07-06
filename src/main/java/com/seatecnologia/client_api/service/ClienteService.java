package com.seatecnologia.client_api.service;

import com.seatecnologia.client_api.DTO.*;
import com.seatecnologia.client_api.entity.Cliente;
import com.seatecnologia.client_api.entity.ClienteEmail;
import com.seatecnologia.client_api.entity.ClienteEndereco;
import com.seatecnologia.client_api.entity.ClienteTelefone;
import com.seatecnologia.client_api.exception.ClienteNaoEncontradoException;
import com.seatecnologia.client_api.exception.CpfDuplicadoException;
import com.seatecnologia.client_api.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Validated
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponseDTO criarCliente(@Valid ClienteRequestDTO requestDto) {

        String cpfNormalizado = removerMascara(requestDto.cpf());
        if (clienteRepository.existsByCpf(cpfNormalizado)) {
            throw new CpfDuplicadoException();
        }

        Cliente novoCliente = new Cliente();
        novoCliente.setNome(requestDto.nome());
        novoCliente.setCpf(cpfNormalizado);

        List<ClienteTelefone> telefones = requestDto.telefones().stream()
                .map(t -> new ClienteTelefone(null, novoCliente, t.tipo(), normalizarTelefone(t)))
                .toList();
        novoCliente.setTelefones(telefones);

        List<ClienteEmail> emails = requestDto.emails().stream()
                .map(email -> new ClienteEmail(null, novoCliente, email))
                .toList();
        novoCliente.setEmails(emails);

        EnderecoDTO enderecoDto = requestDto.endereco();
        ClienteEndereco endereco = new ClienteEndereco(
                null,
                novoCliente,
                removerMascara(enderecoDto.cep()),
                enderecoDto.logradouro(),
                enderecoDto.bairro(),
                enderecoDto.cidade(),
                enderecoDto.uf(),
                enderecoDto.complemento()
        );
        novoCliente.setEndereco(endereco);

        Cliente clienteSalvo = clienteRepository.save(novoCliente);
        return mapToDTO(clienteSalvo);
    }

    private static String removerMascara(String valor) {
        if (valor == null) {
            return null;
        }

        return valor.replaceAll("\\D", "");
    }

    private static String normalizarTelefone(TelefoneDTO telefoneDto) {
        String numeroLimpo = removerMascara(telefoneDto.numero());
        int tamanhoEsperado = "CELULAR".equals(telefoneDto.tipo()) ? 11 : 10;

        if (numeroLimpo == null || numeroLimpo.length() != tamanhoEsperado) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Número de telefone inválido para o tipo " + telefoneDto.tipo() + ": esperado " + tamanhoEsperado + " dígitos.");
        }

        return numeroLimpo;
    }

    private static String mascararCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private static String mascararCep(String cep) {
        if (cep == null || cep.length() != 8) {
            return cep;
        }
        return cep.replaceFirst("(\\d{5})(\\d{3})", "$1-$2");
    }

    private static String mascararTelefone(String numero) {
        if (numero == null) {
            return null;
        }
        if (numero.length() == 11) {
            return numero.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
        if (numero.length() == 10) {
            return numero.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return numero;
    }

    public ClienteResponseDTO listarPorId(Long id) {
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
        return mapToDTO(cliente);
    }

    public List<ClienteResponseDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public void atualizarCliente(Long id, @Valid ClienteRequestDTO requestdto) {
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        if (requestdto.nome() != null) {
            cliente.setNome(requestdto.nome());
        }

        if (requestdto.cpf() != null) {
            String cpfNormalizado = removerMascara(requestdto.cpf());
            if (clienteRepository.existsByCpfAndIdNot(cpfNormalizado, id)) {
                throw new CpfDuplicadoException();
            }
            cliente.setCpf(cpfNormalizado);
        }

        if (requestdto.telefones() != null) {
            cliente.getTelefones().clear();
            requestdto.telefones().stream()
                    .map(t -> new ClienteTelefone(null, cliente, t.tipo(), normalizarTelefone(t)))
                    .forEach(cliente.getTelefones()::add);
        }

        if (requestdto.emails() != null) {
            cliente.getEmails().clear();
            requestdto.emails().stream()
                    .map(email -> new ClienteEmail(null, cliente, email))
                    .forEach(cliente.getEmails()::add);
        }

        if (requestdto.endereco() != null) {
            EnderecoDTO enderecoDto = requestdto.endereco();
            cliente.setEndereco(new ClienteEndereco(
                    null,
                    cliente,
                    removerMascara(enderecoDto.cep()),
                    enderecoDto.logradouro(),
                    enderecoDto.bairro(),
                    enderecoDto.cidade(),
                    enderecoDto.uf(),
                    enderecoDto.complemento()
            ));
        }

        clienteRepository.save(cliente);
    }

    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNaoEncontradoException(id);
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponseDTO mapToDTO(Cliente cliente) {
        List<TelefoneDTO> telefones = cliente.getTelefones()
                .stream()
                .map(t -> new TelefoneDTO(
                        t.getTipo(),
                        mascararTelefone(t.getNumero())
                ))
                .toList();

        List<String> emails = cliente.getEmails()
                .stream()
                .map(ClienteEmail::getEmail)
                .toList();

        EnderecoDTO endereco = new EnderecoDTO(
                mascararCep(cliente.getEndereco().getCep()),
                cliente.getEndereco().getLogradouro(),
                cliente.getEndereco().getBairro(),
                cliente.getEndereco().getCidade(),
                cliente.getEndereco().getUf(),
                cliente.getEndereco().getComplemento()
        );

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                mascararCpf(cliente.getCpf()),
                telefones,
                emails,
                endereco
        );
    }
}

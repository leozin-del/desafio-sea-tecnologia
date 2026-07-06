package com.seatecnologia.client_api.controller;

import com.seatecnologia.client_api.DTO.ClienteRequestDTO;
import com.seatecnologia.client_api.DTO.ClienteResponseDTO;
import com.seatecnologia.client_api.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criarNovoCliente(@RequestBody @Valid ClienteRequestDTO clienteRequestDTO) {
        var clienteCriado = clienteService.criarCliente(clienteRequestDTO);
        URI location = URI.create("/cliente/" + clienteCriado.id());
        return ResponseEntity.created(location).body(clienteCriado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.listarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarCliente (@PathVariable Long id,
                                                  @RequestBody @Valid ClienteRequestDTO atualizarClientedto) {
        clienteService.atualizarCliente(id, atualizarClientedto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }

}

package com.seatecnologia.client_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente_endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEndereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, length = 8)
    private String cep;

    private String logradouro;
    private String bairro;
    private String cidade;

    @Column(length = 2)
    private String uf;

    private String complemento;
}

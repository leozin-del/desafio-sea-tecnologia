package com.seatecnologia.client_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente_telefone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteTelefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    private String tipo;
    private String numero;
}

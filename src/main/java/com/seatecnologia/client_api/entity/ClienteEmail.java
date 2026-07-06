package com.seatecnologia.client_api.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    private String email;
}

package com.projeto.drones.drones_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exemplos_drones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ExemploDrone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String categoria; // Ex: Agricultura, Segurança, Construção

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column
    private String linkDocumentacao; // URL de PDF ou artigo
}

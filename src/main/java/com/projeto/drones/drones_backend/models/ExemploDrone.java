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
    private String nome; // Nome do projeto

    @Column(nullable = false)
    private String categoria; // Ex: Agricultura, Segurança, Construção

    @Column(nullable = false, columnDefinition = "TEXT")
    private String resumo; // Pequeno resumo antes da descrição

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao; // Descrição detalhada do caso de uso

    @Column
    private String imagemUrl; // URL da imagem ilustrativa

    @Column
    private String linkDocumentacao; // URL de PDF ou artigo técnico


}

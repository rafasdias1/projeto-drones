package com.projeto.drones.drones_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String fabricante;

    @Column(nullable = false)
    private double preco;

    @Column(nullable = false)
    private double autonomia;

    @Column(nullable = false)
    private double peso;

    @Column(nullable = false)  // Remova columnDefinition = "TEXT"
    private String sensores;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @Column(nullable = true)
    private String imagemUrl;


}




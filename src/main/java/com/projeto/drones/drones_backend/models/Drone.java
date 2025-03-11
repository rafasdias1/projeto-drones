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
    private String categoria; // Ex: Agricultura, Engenharia, Segurança

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private String imagemUrl;

    @Column(nullable = false)
    private int autonomia; // minutos

    @Column(nullable = false)
    private double peso; // kg

    @Column(nullable = false)
    private double cargaMaxima; // kg

    @Column(nullable = false)
    private double velocidadeMaxima; // km/h

    @Column(nullable = false)
    private double alcanceMaximo; // km

    @Column(nullable = false)
    private String sensores; // Ex: Câmera RGB, LIDAR

    @Column(nullable = false)
    private String resolucaoCamera; // Ex: 48MP, 8K 30fps

    @Column(nullable = false)
    private String gps; // Ex: GPS + RTK

    @Column(nullable = false)
    private String sistemaAnticolisao; // Ex: 360° Deteção de Obstáculos

    @Column(nullable = false)
    private String conectividade; // Ex: Wi-Fi, 4G

    @Column(nullable = false)
    private String modosVoo; // Ex: Waypoints, Seguimento

    @Column(nullable = false)
    private String certificacao; // Ex: ANAC, FAA

    @Column(nullable = false)
    private String failSafe; // Ex: Retorno Automático

    @Column(nullable = false)
    private double precoMin; // Preço mínimo

    @Column(nullable = false)
    private double precoMax; // Preço máximo
}



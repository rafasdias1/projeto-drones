package com.projeto.drones.drones_backend.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DroneComparacaoDTO {

    private String nome;
    private String fabricante;
    private String categoria; // Ex: Agricultura, Engenharia, Segurança
    private String descricao;
    private String imagemUrl;
    private int autonomia; // minutos
    private double peso; // kg
    private double cargaMaxima; // kg
    private double velocidadeMaxima; // km/h
    private double alcanceMaximo; // km
    private String sensores; // Ex: Câmera RGB, LIDAR
    private String resolucaoCamera; // Ex: 48MP, 8K 30fps
    private String gps; // Ex: GPS + RTK
    private String sistemaAnticolisao; // Ex: 360° Deteção de Obstáculos
    private String conectividade; // Ex: Wi-Fi, 4G
    private String modosVoo; // Ex: Waypoints, Seguimento
    private String certificacao; // Ex: ANAC, FAA
    private String failSafe; // Ex: Retorno Automático
    private double precoMin; // Preço mínimo
    private double precoMax; // Preço máximo

}

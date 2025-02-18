package com.projeto.drones.drones_backend.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DroneComparacaoDTO {
    private String nome;
    private String fabricante;
    private double preco;
    private double autonomia;
    private double peso;
    private String sensores;
}

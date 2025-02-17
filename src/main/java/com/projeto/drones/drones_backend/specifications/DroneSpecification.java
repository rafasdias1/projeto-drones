package com.projeto.drones.drones_backend.specifications;

import com.projeto.drones.drones_backend.models.Drone;
import org.springframework.data.jpa.domain.Specification;

public class DroneSpecification {

    public static Specification<Drone> filtrarPorPreco(Double precoMin, Double precoMax) {
        return (root, query, criteriaBuilder) ->
                (precoMin == null && precoMax == null) ? null :
                        criteriaBuilder.between(root.get("preco"),
                                precoMin != null ? precoMin : Double.MIN_VALUE,
                                precoMax != null ? precoMax : Double.MAX_VALUE);
    }

    public static Specification<Drone> filtrarPorAutonomia(Double autonomiaMin, Double autonomiaMax) {
        return (root, query, criteriaBuilder) ->
                (autonomiaMin == null && autonomiaMax == null) ? null :
                        criteriaBuilder.between(root.get("autonomia"),
                                autonomiaMin != null ? autonomiaMin : Double.MIN_VALUE,
                                autonomiaMax != null ? autonomiaMax : Double.MAX_VALUE);
    }

    public static Specification<Drone> filtrarPorFabricante(String fabricante) {
        return (root, query, criteriaBuilder) ->
                (fabricante == null) ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("fabricante")),
                                "%" + fabricante.toLowerCase() + "%");
    }

    public static Specification<Drone> filtrarPorPeso(Double pesoMax) {
        return (root, query, criteriaBuilder) ->
                (pesoMax == null) ? null :
                        criteriaBuilder.lessThanOrEqualTo(root.get("peso"), pesoMax);
    }

    public static Specification<Drone> filtrarPorSensores(String sensores) {
        return (root, query, criteriaBuilder) ->
                (sensores == null) ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("sensores")),
                                "%" + sensores.toLowerCase() + "%");
    }
}

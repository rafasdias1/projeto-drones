package com.projeto.drones.drones_backend.services;

import com.projeto.drones.drones_backend.models.Drone;
import com.projeto.drones.drones_backend.repositories.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import java.util.Optional;

@Service

public class DroneService {
    @Autowired
    private DroneRepository droneRepository;

    public List<Drone> listarDrones(){
        return droneRepository.findAll();
    }

    public Optional<Drone> pesquisarPorId(Long id){
        return droneRepository.findById(id);
    }



    public Drone guardar(Drone drone){
        return droneRepository.save(drone);
    }

    public void eliminar(Long id){
        droneRepository.deleteById(id);
    }

    public List<Drone> pesquisarDrones(Double precoMin, Double precoMax, Double autonomiaMin, Double autonomiaMax,
                                       String fabricante, Double pesoMax, String sensores) {
        Specification<Drone> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (precoMin != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("preco"), precoMin));
            if (precoMax != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("preco"), precoMax));
            if (autonomiaMin != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("autonomia"), autonomiaMin));
            if (autonomiaMax != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("autonomia"), autonomiaMax));
            if (pesoMax != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("peso"), pesoMax));
            if (fabricante != null && !fabricante.isBlank())
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("fabricante")), "%" + fabricante.toLowerCase() + "%"));
            if (sensores != null && !sensores.isBlank())
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("sensores")), "%" + sensores.toLowerCase() + "%"));
            return predicate;
        };
        return droneRepository.findAll(spec);
    }
}

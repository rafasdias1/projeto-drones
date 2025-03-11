package com.projeto.drones.drones_backend.repositories;

import com.projeto.drones.drones_backend.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long>, JpaSpecificationExecutor<Drone> {

    @Query("SELECT d FROM Drone d WHERE " +
            "(:precoMin IS NULL OR d.precoMin >= :precoMin) AND " +
            "(:precoMax IS NULL OR d.precoMax <= :precoMax)")
    List<Drone> findByPrecoRange(Double precoMin, Double precoMax);
}

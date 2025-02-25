package com.projeto.drones.drones_backend.repositories;

import com.projeto.drones.drones_backend.models.ExemploDrone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExemploDroneRepository extends JpaRepository<ExemploDrone, Long> {
    List<ExemploDrone> findByCategoriaIgnoreCase(String categoria);
}
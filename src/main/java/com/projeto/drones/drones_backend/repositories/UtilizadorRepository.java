package com.projeto.drones.drones_backend.repositories;

import com.projeto.drones.drones_backend.models.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilizadorRepository extends JpaRepository<Utilizador,Long> {
    Optional<Utilizador> findByEmail (String email);
}

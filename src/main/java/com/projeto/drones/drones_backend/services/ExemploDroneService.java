package com.projeto.drones.drones_backend.services;

import com.projeto.drones.drones_backend.models.ExemploDrone;
import com.projeto.drones.drones_backend.repositories.ExemploDroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExemploDroneService {

    @Autowired
    private ExemploDroneRepository exemploDroneRepository;

    public List<ExemploDrone> listarTodos() {
        return exemploDroneRepository.findAll();
    }

    public Optional<ExemploDrone> pesquisarPorId(Long id) {
        return exemploDroneRepository.findById(id);
    }

    public List<ExemploDrone> pesquisarPorCategoria(String categoria) {
        return exemploDroneRepository.findByCategoriaIgnoreCase(categoria);
    }

    public ExemploDrone guardar(ExemploDrone exemplo) {
        return exemploDroneRepository.save(exemplo);
    }

    public void eliminar(Long id) {
        exemploDroneRepository.deleteById(id);
    }
}

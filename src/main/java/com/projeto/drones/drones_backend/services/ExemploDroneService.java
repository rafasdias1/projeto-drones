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

    // 🔹 Listar todos os exemplos
    public List<ExemploDrone> listarTodos() {
        return exemploDroneRepository.findAll();
    }

    // 🔹 Buscar por ID
    public Optional<ExemploDrone> pesquisarPorId(Long id) {
        return exemploDroneRepository.findById(id);
    }

    // 🔹 Buscar por categoria (ignora maiúsculas/minúsculas)
    public List<ExemploDrone> pesquisarPorCategoria(String categoria) {
        return exemploDroneRepository.findByCategoriaIgnoreCase(categoria.trim());
    }

    // 🔹 Salvar um novo exemplo (com validação)
    public ExemploDrone guardar(ExemploDrone exemplo) {
        if (exemplo.getNome() == null || exemplo.getDescricao() == null || exemplo.getCategoria() == null) {
            throw new IllegalArgumentException("Todos os campos obrigatórios devem ser preenchidos.");
        }
        return exemploDroneRepository.save(exemplo);
    }

    // 🔹 Excluir um exemplo (com validação)
    public void eliminar(Long id) {
        if (!exemploDroneRepository.existsById(id)) {
            throw new IllegalArgumentException("O exemplo de drone com ID " + id + " não existe.");
        }
        exemploDroneRepository.deleteById(id);
    }
}

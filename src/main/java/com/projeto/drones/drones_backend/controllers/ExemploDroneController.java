package com.projeto.drones.drones_backend.controllers;

import com.projeto.drones.drones_backend.models.ExemploDrone;
import com.projeto.drones.drones_backend.services.ExemploDroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exemplos-drones")
public class ExemploDroneController {

    @Autowired
    private ExemploDroneService exemploDroneService;

    @GetMapping
    public ResponseEntity<List<ExemploDrone>> listarTodos() {
        return ResponseEntity.ok(exemploDroneService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExemploDrone> pesquisarPorId(@PathVariable Long id) {
        Optional<ExemploDrone> exemplo = exemploDroneService.pesquisarPorId(id);
        return exemplo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<ExemploDrone>> pesquisarPorCategoria(@RequestParam String categoria) {
        return ResponseEntity.ok(exemploDroneService.pesquisarPorCategoria(categoria));
    }

    @PostMapping
    public ResponseEntity<ExemploDrone> adicionar(@RequestBody ExemploDrone exemplo) {
        return ResponseEntity.ok(exemploDroneService.guardar(exemplo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (exemploDroneService.pesquisarPorId(id).isPresent()) {
            exemploDroneService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    // Atualizar um exemplo prático de drone
    @PutMapping("/{id}")
    public ResponseEntity<ExemploDrone> atualizarExemplo(@PathVariable Long id, @RequestBody ExemploDrone exemploAtualizado) {
        Optional<ExemploDrone> exemploExistente = exemploDroneService.pesquisarPorId(id);

        if (exemploExistente.isPresent()) {
            ExemploDrone exemplo = exemploExistente.get();
            exemplo.setNome(exemploAtualizado.getNome());
            exemplo.setDescricao(exemploAtualizado.getDescricao());
            exemplo.setCategoria(exemploAtualizado.getCategoria());
            exemplo.setLinkDocumentacao(exemploAtualizado.getLinkDocumentacao());

            ExemploDrone atualizado = exemploDroneService.guardar(exemplo);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }
}
package com.projeto.drones.drones_backend.controllers;

import com.projeto.drones.drones_backend.dto.DroneComparacaoDTO;
import com.projeto.drones.drones_backend.models.Drone;
import com.projeto.drones.drones_backend.services.DroneService;
import com.projeto.drones.drones_backend.services.PdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;

    @Autowired
    private PdfExportService pdfExportService;

    // Endpoint para listar todos os drones
    @GetMapping
    public List<Drone> listarDrones() {
        return droneService.listarDrones();
    }

    // Endpoint para pesquisar drone por ID
    @GetMapping("/{id}")
    public ResponseEntity<Drone> buscarPorId(@PathVariable Long id) {
        Optional<Drone> drone = droneService.pesquisarPorId(id);
        return drone.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint para criar ou atualizar drone
    @PostMapping
    public ResponseEntity<Drone> criarOuAtualizarDrone(@RequestBody Drone drone) {
        Drone savedDrone = droneService.guardar(drone);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDrone);
    }

    // Endpoint para eliminar drone por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDrone(@PathVariable Long id) {
        droneService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Endpoint para pesquisa avançada de drones com filtros
    @GetMapping("/pesquisar")
    public Page<Drone> pesquisarDrones(
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) Double autonomiaMin,
            @RequestParam(required = false) Double autonomiaMax,
            @RequestParam(required = false) String fabricante,
            @RequestParam(required = false) Double pesoMax,
            @RequestParam(required = false) String sensores,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double alcanceMin,
            @RequestParam(required = false) Double velocidadeMin,
            Pageable pageable) {
        return droneService.pesquisarDrones(precoMin, precoMax, autonomiaMin, autonomiaMax, fabricante, pesoMax, sensores, categoria, alcanceMin, velocidadeMin, pageable);
    }
    @PostMapping("/comparar")
    public ResponseEntity<?> compararDrones(@RequestBody List<Long> ids) {
        try {
            List<DroneComparacaoDTO> comparacao = droneService.compararDrones(ids);
            return ResponseEntity.ok(comparacao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Método para exportar comparação em PDF
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportarComparacaoPdf(@RequestParam List<Long> ids) {
        List<Drone> drones = droneService.listarPorIds(ids);

        byte[] pdf = pdfExportService.gerarPdfComparacao(drones);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comparacao_drones.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}


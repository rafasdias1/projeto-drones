package com.projeto.drones.drones_backend.controllers;

import com.projeto.drones.drones_backend.dto.DroneComparacaoDTO;
import com.projeto.drones.drones_backend.models.Drone;
import com.projeto.drones.drones_backend.services.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;


    @GetMapping
    public List<Drone> listarDrones(){
        return droneService.listarDrones();
    }



    @GetMapping("/{id}")
    public ResponseEntity<Drone> pesquisarPorId(@PathVariable long id){
        Optional<Drone> drone= droneService.pesquisarPorId(id);
        return drone.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Drone> guardar (@RequestBody Drone drone){
        return ResponseEntity.ok(droneService.guardar(drone));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar (@PathVariable Long id){
        if(droneService.pesquisarPorId(id).isPresent()){
            droneService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



    @PutMapping("/{id}")
    public ResponseEntity<Drone> atualizarDrone(@PathVariable long id, @RequestBody Drone droneAtualizado){
        Optional<Drone> droneExistente = droneService.pesquisarPorId(id);
        if(droneExistente.isPresent()){
            Drone drone= droneExistente.get();
            drone.setNome(droneAtualizado.getNome());
            drone.setFabricante(droneAtualizado.getFabricante());
            drone.setPreco(droneAtualizado.getPreco());
            drone.setAutonomia(droneAtualizado.getAutonomia());
            drone.setPeso(droneAtualizado.getPeso());
            drone.setSensores(droneAtualizado.getSensores());
            drone.setDescricao(droneAtualizado.getDescricao());
            drone.setImagemUrl(droneAtualizado.getImagemUrl());

            Drone atualizado= droneService.guardar(drone);

            return ResponseEntity.ok(atualizado);

        }else{
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> pesquisarDrones(
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) Double autonomiaMin,
            @RequestParam(required = false) Double autonomiaMax,
            @RequestParam(required = false) String fabricante,
            @RequestParam(required = false) Double pesoMax,
            @RequestParam(required = false) String sensores,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome,asc") String[] sort) {

        // Configurar ordenação
        String sortBy = sort[0];
        String direction = (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) ? "desc" : "asc";
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        // Chamar o serviço
        Page<Drone> pageResult = droneService.pesquisarDrones(precoMin, precoMax, autonomiaMin, autonomiaMax, fabricante, pesoMax, sensores, pageable);

        Map<String, Object> response = Map.of(
                "drones", pageResult.getContent(),
                "currentPage", pageResult.getNumber(),
                "totalItems", pageResult.getTotalElements(),
                "totalPages", pageResult.getTotalPages()
        );
        return ResponseEntity.ok(response);
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


}
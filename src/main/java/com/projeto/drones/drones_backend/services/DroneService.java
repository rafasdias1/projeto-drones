package com.projeto.drones.drones_backend.services;

import com.projeto.drones.drones_backend.dto.DroneComparacaoDTO;
import com.projeto.drones.drones_backend.models.Drone;
import com.projeto.drones.drones_backend.repositories.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    // Listar todos os drones
    public List<Drone> listarDrones() {
        return droneRepository.findAll();
    }

    // Buscar drone por ID
    public Optional<Drone> pesquisarPorId(Long id) {
        return droneRepository.findById(id);
    }

    // Criar ou atualizar drone
    public Drone guardar(Drone drone) {
        return droneRepository.save(drone);
    }

    // Deletar drone por ID
    public void eliminar(Long id) {
        droneRepository.deleteById(id);
    }

    // Pesquisa avançada com filtros
    public Page<Drone> pesquisarDrones(
            Double precoMin, Double precoMax,
            Double autonomiaMin, Double autonomiaMax,
            String fabricante, Double pesoMax,
            String sensores, String categoria, Double alcanceMin, Double velocidadeMin,
            Pageable pageable) {

        Specification<Drone> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            // Filtros de pesquisa
            if (precoMin != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("precoMin"), precoMin));
            if (precoMax != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("precoMax"), precoMax));
            if (autonomiaMin != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("autonomia"), autonomiaMin));
            if (autonomiaMax != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("autonomia"), autonomiaMax));
            if (pesoMax != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("peso"), pesoMax));
            if (fabricante != null && !fabricante.isBlank())
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("fabricante")), "%" + fabricante.toLowerCase() + "%"));
            if (sensores != null && !sensores.isBlank())
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("sensores")), "%" + sensores.toLowerCase() + "%"));
            if (categoria != null && !categoria.isBlank())
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("categoria")), "%" + categoria.toLowerCase() + "%"));
            if (alcanceMin != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("alcanceMaximo"), alcanceMin));
            if (velocidadeMin != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("velocidadeMaxima"), velocidadeMin));

            return predicate;
        };

        return droneRepository.findAll(spec, pageable);
    }

    // Método para comparar drones
    public List<DroneComparacaoDTO> compararDrones(List<Long> ids) {
        List<Drone> drones = droneRepository.findAllById(ids);

        if (drones.size() != ids.size()) {
            throw new IllegalArgumentException("Alguns drones não foram encontrados.");
        }

        // Mapear drones para DroneComparacaoDTO
        return drones.stream()
                .map(drone -> new DroneComparacaoDTO(
                        drone.getNome(),
                        drone.getFabricante(),
                        drone.getCategoria(),
                        drone.getDescricao(),
                        drone.getImagemUrl(),
                        drone.getAutonomia(),
                        drone.getPeso(),
                        drone.getCargaMaxima(),
                        drone.getVelocidadeMaxima(),
                        drone.getAlcanceMaximo(),
                        drone.getSensores(),
                        drone.getResolucaoCamera(),
                        drone.getGps(),
                        drone.getSistemaAnticolisao(),
                        drone.getConectividade(),
                        drone.getModosVoo(),
                        drone.getCertificacao(),
                        drone.getFailSafe(),
                        drone.getPrecoMin(),
                        drone.getPrecoMax()
                ))
                .collect(Collectors.toList());
    }

    // Método para listar drones por ID (utilizado para exportação PDF)
    public List<Drone> listarPorIds(List<Long> ids) {
        return droneRepository.findAllById(ids);
    }
}


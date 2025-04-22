package com.projeto.drones.drones_backend.services;

import com.projeto.drones.drones_backend.models.Drone;
import com.projeto.drones.drones_backend.repositories.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DroneServiceTest {

    private DroneRepository droneRepository;
    private DroneService droneService;

    @BeforeEach
    void setUp() {
        droneRepository = mock(DroneRepository.class);
        droneService = new DroneService();
        // Injeta o mock manualmente
        droneService = spy(droneService); // se precisar sobrepor métodos
        droneService.getClass().getDeclaredFields();
        // Alternativamente, podes reescrever DroneService para ter construtor
    }

    @Test
    void testListarDrones() {
        List<Drone> drones = List.of(new Drone(), new Drone());
        when(droneRepository.findAll()).thenReturn(drones);

        List<Drone> result = droneService.listarDrones();

        assertEquals(2, result.size());
        verify(droneRepository, times(1)).findAll();
    }

    @Test
    void testPesquisarPorId() {
        Drone drone = new Drone();
        drone.setId(1L);

        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));

        Optional<Drone> result = droneService.pesquisarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGuardarDrone() {
        Drone drone = new Drone();
        drone.setNome("Teste");

        when(droneRepository.save(drone)).thenReturn(drone);

        Drone result = droneService.guardar(drone);

        assertEquals("Teste", result.getNome());
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    void testEliminarDrone() {
        Long id = 1L;
        doNothing().when(droneRepository).deleteById(id);

        droneService.eliminar(id);

        verify(droneRepository, times(1)).deleteById(id);
    }

    @Test
    void testCompararDronesComIdsInvalidos() {
        when(droneRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(new Drone())); // só 1 encontrado

        assertThrows(IllegalArgumentException.class, () -> {
            droneService.compararDrones(List.of(1L, 2L));
        });
    }
}

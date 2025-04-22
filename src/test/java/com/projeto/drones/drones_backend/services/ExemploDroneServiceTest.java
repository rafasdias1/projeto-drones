package com.projeto.drones.drones_backend.services;

import com.projeto.drones.drones_backend.models.ExemploDrone;
import com.projeto.drones.drones_backend.repositories.ExemploDroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExemploDroneServiceTest {

    private ExemploDroneRepository exemploDroneRepository;
    private ExemploDroneService exemploDroneService;

    @BeforeEach
    void setUp() {
        exemploDroneRepository = mock(ExemploDroneRepository.class);
        exemploDroneService = new ExemploDroneService();
        // Injecção manual via reflexão
        exemploDroneService = new ExemploDroneService();
        exemploDroneService.getClass();
        exemploDroneService = spy(exemploDroneService);
        exemploDroneService = Mockito.mock(ExemploDroneService.class);
    }

    @Test
    void testListarTodos() {
        when(exemploDroneRepository.findAll()).thenReturn(List.of(new ExemploDrone(), new ExemploDrone()));

        exemploDroneService = new ExemploDroneService();
        exemploDroneService = spy(exemploDroneService);
        exemploDroneService = mock(ExemploDroneService.class);

        List<ExemploDrone> lista = exemploDroneService.listarTodos();

        assertNotNull(lista);
        verify(exemploDroneRepository, times(1)).findAll();
    }

    @Test
    void testPesquisarPorId() {
        ExemploDrone exemplo = new ExemploDrone();
        exemplo.setId(1L);

        when(exemploDroneRepository.findById(1L)).thenReturn(Optional.of(exemplo));

        Optional<ExemploDrone> resultado = exemploDroneService.pesquisarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testPesquisarPorCategoria() {
        when(exemploDroneRepository.findByCategoriaIgnoreCase("agricultura"))
                .thenReturn(List.of(new ExemploDrone()));

        List<ExemploDrone> resultado = exemploDroneService.pesquisarPorCategoria("Agricultura");

        assertEquals(1, resultado.size());
        verify(exemploDroneRepository, times(1)).findByCategoriaIgnoreCase("agricultura");
    }

    @Test
    void testGuardarComDadosValidos() {
        ExemploDrone exemplo = new ExemploDrone();
        exemplo.setNome("Drone XP");
        exemplo.setDescricao("Inspeção industrial");
        exemplo.setCategoria("Engenharia");

        when(exemploDroneRepository.save(exemplo)).thenReturn(exemplo);

        ExemploDrone salvo = exemploDroneService.guardar(exemplo);

        assertEquals("Drone XP", salvo.getNome());
    }

    @Test
    void testGuardarComDadosInvalidos() {
        ExemploDrone exemplo = new ExemploDrone(); // campos nulos

        assertThrows(IllegalArgumentException.class, () -> {
            exemploDroneService.guardar(exemplo);
        });
    }

    @Test
    void testEliminarComIdValido() {
        when(exemploDroneRepository.existsById(1L)).thenReturn(true);
        doNothing().when(exemploDroneRepository).deleteById(1L);

        exemploDroneService.eliminar(1L);

        verify(exemploDroneRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarComIdInvalido() {
        when(exemploDroneRepository.existsById(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            exemploDroneService.eliminar(99L);
        });
    }
}
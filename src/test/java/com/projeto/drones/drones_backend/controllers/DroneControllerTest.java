package com.projeto.drones.drones_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.drones.drones_backend.models.Drone;
import com.projeto.drones.drones_backend.services.DroneService;
import com.projeto.drones.drones_backend.services.PdfExportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DroneController.class)
public class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DroneService droneService;

    @MockitoBean
    private PdfExportService pdfExportService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Deve listar todos os drones")
    public void deveListarTodosDrones() throws Exception {
        Drone drone1 = new Drone();
        drone1.setId(1L);
        drone1.setNome("Drone A");

        Drone drone2 = new Drone();
        drone2.setId(2L);
        drone2.setNome("Drone B");

        Mockito.when(droneService.listarDrones()).thenReturn(Arrays.asList(drone1, drone2));

        mockMvc.perform(get("/api/drones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("✅ Deve buscar drone por ID com sucesso")
    public void deveBuscarDronePorId() throws Exception {
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setNome("Drone Teste");

        Mockito.when(droneService.pesquisarPorId(1L)).thenReturn(Optional.of(drone));

        mockMvc.perform(get("/api/drones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Drone Teste"));
    }

    @Test
    @DisplayName("❌ Deve retornar 404 se drone não for encontrado por ID")
    public void deveRetornarNotFound() throws Exception {
        Mockito.when(droneService.pesquisarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/drones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Deve criar novo drone")
    public void deveCriarDrone() throws Exception {
        Drone novoDrone = new Drone();
        novoDrone.setNome("Novo Drone");

        Drone salvo = new Drone();
        salvo.setId(1L);
        salvo.setNome("Novo Drone");

        Mockito.when(droneService.guardar(Mockito.any(Drone.class))).thenReturn(salvo);

        mockMvc.perform(post("/api/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoDrone)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("✅ Deve eliminar drone por ID")
    public void deveEliminarDrone() throws Exception {
        mockMvc.perform(delete("/api/drones/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(droneService).eliminar(1L);
    }

    @Test
    @DisplayName("✅ Deve exportar comparação em PDF")
    public void deveExportarComparacaoPdf() throws Exception {
        List<Drone> drones = List.of(new Drone(), new Drone());
        byte[] pdfSimulado = new byte[]{1, 2, 3, 4};

        Mockito.when(droneService.listarPorIds(Mockito.anyList())).thenReturn(drones);
        Mockito.when(pdfExportService.gerarPdfComparacao(drones)).thenReturn(pdfSimulado);

        mockMvc.perform(get("/api/drones/export/pdf")
                        .param("ids", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=comparacao_drones.pdf"));
    }
}

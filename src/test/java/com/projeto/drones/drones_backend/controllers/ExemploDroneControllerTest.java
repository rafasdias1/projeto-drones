package com.projeto.drones.drones_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.drones.drones_backend.models.ExemploDrone;
import com.projeto.drones.drones_backend.services.ExemploDroneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExemploDroneController.class)
public class ExemploDroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExemploDroneService exemploDroneService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deveListarTodosOsExemplos() throws Exception {
        ExemploDrone exemplo = new ExemploDrone(1L, "Drone Teste", "Resumo", "Descrição", "Engenharia", "url", "link");
        when(exemploDroneService.listarTodos()).thenReturn(List.of(exemplo));

        mockMvc.perform(get("/api/exemplos-drones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Drone Teste"));
    }

    @Test
    public void deveRetornarExemploPorId() throws Exception {
        ExemploDrone exemplo = new ExemploDrone(1L, "Drone Teste", "Resumo", "Descrição", "Engenharia", "url", "link");
        when(exemploDroneService.pesquisarPorId(1L)).thenReturn(Optional.of(exemplo));

        mockMvc.perform(get("/api/exemplos-drones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Drone Teste"));
    }

    @Test
    public void deveRetornar404QuandoExemploNaoEncontrado() throws Exception {
        when(exemploDroneService.pesquisarPorId(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/exemplos-drones/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveAdicionarExemploValido() throws Exception {
        ExemploDrone exemplo = new ExemploDrone(null, "Drone Novo", "Resumo", "Descrição", "Engenharia", "url", "link");
        when(exemploDroneService.guardar(exemplo)).thenReturn(exemplo);

        mockMvc.perform(post("/api/exemplos-drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exemplo)))
                .andExpect(status().isOk());
    }
}

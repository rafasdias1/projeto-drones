package com.projeto.drones.drones_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.drones.drones_backend.dto.LoginDTO;
import com.projeto.drones.drones_backend.services.AutenticacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutenticacaoController.class)
public class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutenticacaoService autenticacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Deve devolver token ao fazer login com dados válidos")
    public void deveAutenticarComSucesso() throws Exception {
        LoginDTO loginDTO = new LoginDTO();

        String tokenFalso = "jwt.falso.token";
        Mockito.when(autenticacaoService.autenticar(loginDTO.getEmail(), loginDTO.getPassword()))
                .thenReturn(tokenFalso);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(tokenFalso));
    }

    @Test
    @DisplayName("❌ Deve devolver erro ao registar sem campos obrigatórios")
    public void deveFalharAoRegistarSemCampos() throws Exception {
        Map<String, String> dadosIncompletos = Map.of(
                "email", "teste@email.com",
                "password", "" // falta role
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosIncompletos)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    @DisplayName("✅ Deve registar utilizador com sucesso")
    public void deveRegistarComSucesso() throws Exception {
        Map<String, String> dados = Map.of(
                "email", "novo@email.com",
                "password", "admin123",
                "role", "ADMIN"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Utilizador criado com sucesso!"));
    }
}
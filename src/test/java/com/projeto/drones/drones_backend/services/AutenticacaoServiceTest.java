package com.projeto.drones.drones_backend.services;

import com.projeto.drones.drones_backend.repositories.UtilizadorRepository;

import com.projeto.drones.drones_backend.models.Role;
import com.projeto.drones.drones_backend.models.Utilizador;
import com.projeto.drones.drones_backend.services.AutenticacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AutenticacaoServiceTest {

    private AutenticacaoService autenticacaoService;
    private UtilizadorRepository utilizadorRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        utilizadorRepository = mock(UtilizadorRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();

        autenticacaoService = new AutenticacaoService(utilizadorRepository, passwordEncoder);
    }
    @Test
    void deveRegistarNovoUtilizadorComSucesso() {
        String email = "teste@example.com";
        String password = "teste123";

        when(utilizadorRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(utilizadorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Utilizador utilizador = autenticacaoService.registar(email, password, Role.USER);

        assertEquals(email, utilizador.getEmail());
        assertTrue(passwordEncoder.matches(password, utilizador.getPassword()));
    }

    @Test
    void deveLancarExcecaoSeEmailJaExiste() {
        String email = "existente@example.com";
        when(utilizadorRepository.findByEmail(email)).thenReturn(Optional.of(new Utilizador()));

        assertThrows(RuntimeException.class, () -> {
            autenticacaoService.registar(email, "qualquercoisa", Role.USER);
        });
    }

    @Test
    void deveAutenticarComCredenciaisValidas() {
        String email = "valid@example.com";
        String rawPassword = "senha123";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        Utilizador utilizador = new Utilizador();
        utilizador.setEmail(email);
        utilizador.setPassword(hashedPassword);
        utilizador.setRole(Role.USER);

        when(utilizadorRepository.findByEmail(email)).thenReturn(Optional.of(utilizador));

        String token = autenticacaoService.autenticar(email, rawPassword);
        assertNotNull(token);
    }

    @Test
    void deveLancarExcecaoComSenhaInvalida() {
        String email = "user@example.com";

        Utilizador utilizador = new Utilizador();
        utilizador.setEmail(email);
        utilizador.setPassword(passwordEncoder.encode("senhaCorreta"));
        utilizador.setRole(Role.USER);

        when(utilizadorRepository.findByEmail(email)).thenReturn(Optional.of(utilizador));

        assertThrows(RuntimeException.class, () -> {
            autenticacaoService.autenticar(email, "senhaErrada");
        });
    }

    @Test
    void deveLancarExcecaoSeUtilizadorNaoEncontrado() {
        when(utilizadorRepository.findByEmail("naoexiste@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            autenticacaoService.autenticar("naoexiste@example.com", "qualquer");
        });
    }
}

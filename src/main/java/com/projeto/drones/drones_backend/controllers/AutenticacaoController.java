package com.projeto.drones.drones_backend.controllers;


import com.projeto.drones.drones_backend.dto.LoginDTO;
import com.projeto.drones.drones_backend.models.Role;
import com.projeto.drones.drones_backend.services.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("🔹 Email recebido: " + loginDTO.getEmail());
        System.out.println("🔹 Password recebida: " + loginDTO.getPassword());

        String token = autenticacaoService.autenticar(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }
    @PostMapping("/register")
    public ResponseEntity<?> registar (@RequestBody Map<String, String> dados){
        String email= dados.get("email");
        String password=dados.get("password");
        String role = dados.get("role");


        if (email== null || password== null || role==null){
            return ResponseEntity.badRequest().body(Map.of("erro", "Todos os campos são obrigatórios!"));

        }

        try {
            autenticacaoService.registar(email, password, Role.valueOf(role.toUpperCase()));
            return ResponseEntity.ok(Map.of("mensagem", "Utilizador criado com sucesso!"));

        }catch (RuntimeException e){
            return  ResponseEntity.status(400).body(Map.of("erro", e.getMessage()));
        }
    }

}

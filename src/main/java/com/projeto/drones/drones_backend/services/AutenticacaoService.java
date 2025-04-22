package com.projeto.drones.drones_backend.services;

import com.projeto.drones.drones_backend.models.Role;
import com.projeto.drones.drones_backend.models.Utilizador;
import com.projeto.drones.drones_backend.repositories.UtilizadorRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class AutenticacaoService {

    // 🔹 Gera uma chave segura de 32 bytes para assinar o JWT
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "EstaÉUmaChaveMuitoLongaParaJWTComMaisDe32Bytes!".getBytes()
    );


    private UtilizadorRepository utilizadorRepository;


    private BCryptPasswordEncoder passwordEncoder;

    public AutenticacaoService(UtilizadorRepository utilizadorRepository, BCryptPasswordEncoder passwordEncoder) {
        this.utilizadorRepository = utilizadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String autenticar(String email, String password) {
        Optional<Utilizador> utilizador = utilizadorRepository.findByEmail(email);

        if (utilizador.isEmpty()) {
            System.out.println("❌ Utilizador não encontrado: " + email);
            throw new RuntimeException("Dados Inválidos!");
        }

        String senhaDigitada = password;
        String senhaEncriptada = utilizador.get().getPassword();

        System.out.println("🔹 Senha digitada: " + senhaDigitada);
        System.out.println("🔹 Senha encriptada na BD: " + senhaEncriptada);

        if (!passwordEncoder.matches(senhaDigitada, senhaEncriptada)) {
            System.out.println("❌ Senha incorreta para o utilizador: " + email);
            throw new RuntimeException("Dados Inválidos!");
        }

        System.out.println("✅ Login bem-sucedido: " + email);
        return gerarToken(utilizador.get());
    }



    public Utilizador registar(String email, String password, Role role) {
        if (utilizadorRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Este email já está registado!");
        }

        Utilizador novoUtilizador = new Utilizador();
        novoUtilizador.setEmail(email);
        novoUtilizador.setPassword(passwordEncoder.encode(password));
        novoUtilizador.setRole(role);

        return utilizadorRepository.save(novoUtilizador);
    }

    private String gerarToken(Utilizador utilizador) {
        return Jwts.builder()
                .setSubject(utilizador.getEmail())
                .claim("role", "ROLE_" + utilizador.getRole().name())  // 🔹 Agora adicionamos "ROLE_"
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

}

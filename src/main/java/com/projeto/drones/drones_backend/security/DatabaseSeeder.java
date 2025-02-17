package com.projeto.drones.drones_backend.security;


import com.projeto.drones.drones_backend.models.Role;
import com.projeto.drones.drones_backend.models.Utilizador;
import com.projeto.drones.drones_backend.repositories.UtilizadorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initAdminUser (UtilizadorRepository utilizadorRepository, BCryptPasswordEncoder passwordEncoder){
        return args -> {
            String email= "admin@email.com";
            String password="admin123";

            if (utilizadorRepository.findByEmail(email).isEmpty()){
                Utilizador admin= new Utilizador();
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode(password));
                admin.setRole(Role.ADMIN);
                utilizadorRepository.save(admin);
            }
        };
    }
}

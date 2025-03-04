package com.projeto.drones.drones_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 🔹 Permite TODAS as rotas do backend serem acessadas
                .allowedOrigins("http://localhost:5174", "http://127.0.0.1:5174") // 🔹 Permite o frontend acessar
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 🔹 Permite os métodos HTTP
                .allowedHeaders("*") // 🔹 Permite qualquer cabeçalho
                .allowCredentials(true); // 🔹 Permite o envio de cookies (se necessário)
    }
}

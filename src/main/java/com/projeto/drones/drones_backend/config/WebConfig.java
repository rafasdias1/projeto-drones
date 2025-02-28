package com.projeto.drones.drones_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Permite todas as requisições que comecem com /api/
                .allowedOrigins("http://127.0.0.1:5174/") // Permite qualquer origem (CUIDADO para produção!)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permite esses métodos HTTP
                .allowedHeaders("*"); // Permite qualquer cabeçalho (se necessário)
    }
}

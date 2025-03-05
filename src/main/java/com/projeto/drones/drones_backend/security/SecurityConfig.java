package com.projeto.drones.drones_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // ✅ PERMITIR OPTIONS PARA TODAS AS ROTAS (Evita bloqueios do navegador)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🟢 Endpoints de Autenticação
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()

                        // 🟢 Endpoints de Drones
                        .requestMatchers(HttpMethod.GET, "/api/drones/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/drones").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/drones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/drones/**").hasRole("ADMIN")

                        // 🟢 Endpoints de Zonas Aéreas
                        .requestMatchers(HttpMethod.GET, "/api/airspaces/**").permitAll()

                        // 🟢 Endpoints de Exemplos Práticos
                        .requestMatchers(HttpMethod.GET, "/api/exemplos-drones/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/exemplos-drones").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/exemplos-drones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/exemplos-drones/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


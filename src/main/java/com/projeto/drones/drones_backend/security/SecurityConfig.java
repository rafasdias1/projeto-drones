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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // 🟢 Endpoints de Autenticação (Login e Registro) - Qualquer um pode acessar
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()


                        // 🟢 Endpoints de Drones - Somente ADMIN pode criar/editar/excluir, mas qualquer um pode visualizar
                        .requestMatchers(HttpMethod.POST, "/api/drones").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/drones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/drones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/drones/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/drones/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/drones/comparar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/drones/export").permitAll()

                        // 🟢 Endpoints de Zonas Aéreas - Qualquer um pode acessar
                        .requestMatchers(HttpMethod.GET, "/api/airspaces").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/airspaces/geojson").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/airspaces/check").permitAll()

                        // 🟢 Endpoints de Exemplos Práticos - Apenas ADMIN pode adicionar, editar e remover
                        .requestMatchers(HttpMethod.POST, "/api/exemplos-drones").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/exemplos-drones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/exemplos-drones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/exemplos-drones/**").permitAll()

                        .anyRequest().authenticated()

                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

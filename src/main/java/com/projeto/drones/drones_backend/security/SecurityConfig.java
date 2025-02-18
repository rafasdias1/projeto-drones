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
                        .requestMatchers(HttpMethod.POST, "/api/drones").hasRole("ADMIN")  // Apenas ADMIN pode criar drones
                        .requestMatchers(HttpMethod.PUT, "/api/drones/**").hasRole("ADMIN")  // Apenas ADMIN pode editar drones
                        .requestMatchers(HttpMethod.DELETE, "/api/drones/**").hasRole("ADMIN")  // Apenas ADMIN pode apagar drones
                        .requestMatchers(HttpMethod.GET, "/api/drones/**").permitAll()  // Qualquer pessoa pode visualizar drones
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/drones/search").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/drones/comparar").permitAll()

                        .anyRequest().authenticated()

                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

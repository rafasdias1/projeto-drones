package com.projeto.drones.drones_backend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class OpenAipApiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey = "8159ce0c79a6ecf1a365ea85deb15f19";
    private final String openAipUrl = "https://api.core.openaip.net/api/airspaces";

    public OpenAipApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Map<String, Object>> getAirspaces() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-openaip-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                openAipUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        try {

            JsonNode rootNode = objectMapper.readTree(response.getBody());


            return objectMapper.convertValue(rootNode.get("items"), List.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta da OpenAIP: " + e.getMessage());
        }
    }
}
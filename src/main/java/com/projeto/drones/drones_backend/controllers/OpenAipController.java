package com.projeto.drones.drones_backend.controllers;

import com.projeto.drones.drones_backend.services.OpenAipGeoJsonService;
import com.projeto.drones.drones_backend.services.OpenAipService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/airspaces")
public class OpenAipController {

    private final OpenAipService openAipService;
    private final OpenAipGeoJsonService openAipGeoJsonService;

    public OpenAipController(OpenAipService openAipService, OpenAipGeoJsonService openAipGeoJsonService) {
        this.openAipService = openAipService;
        this.openAipGeoJsonService = openAipGeoJsonService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAirspaces() {
        return ResponseEntity.ok(openAipService.getAirspaces());
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkFlight(@RequestParam double lat, @RequestParam double lon) {
        return ResponseEntity.ok(openAipService.checkFlightPermission(lat, lon));
    }

    @GetMapping("/geojson")
    public ResponseEntity<String> getAirspacesGeoJson() {
        List<Map<String, Object>> airspaces = openAipService.getAirspaces();
        JSONObject geoJsonObject = openAipGeoJsonService.formatToGeoJson(airspaces);  // Assuming it returns JSONObject
        String geoJson = geoJsonObject.toString();  // Convert JSONObject to String
        return ResponseEntity.ok(geoJson);
    }


}


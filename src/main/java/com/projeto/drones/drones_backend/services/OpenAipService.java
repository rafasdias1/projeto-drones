package com.projeto.drones.drones_backend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenAipService {
    private final OpenAipApiClient openAipApiClient;

    public OpenAipService(OpenAipApiClient openAipApiClient) {
        this.openAipApiClient = openAipApiClient;
    }
    public List<Map<String, Object>> getAirspaces() {
        return openAipApiClient.getAirspaces();
    }

    public List<Map<String, Object>> getFilteredAirspaces(String country) {
        List<Map<String, Object>> airspaces = getAirspaces();

        if (country != null && !country.isBlank()) {
            return airspaces.stream()
                    .filter(airspace -> country.equalsIgnoreCase((String) airspace.get("country")))
                    .collect(Collectors.toList());
        }
        return airspaces;
    }


    public Map<String, Object> checkFlightPermission(double latitude, double longitude) {
        List<Map<String, Object>> airspaces = openAipApiClient.getAirspaces();

        for (Map<String, Object> airspace : airspaces) {
            Map<String, Object> geometry = (Map<String, Object>) airspace.get("geometry");

            if (geometry == null || !"Polygon".equals(geometry.get("type"))) {
                continue;
            }

            List<List<List<Double>>> allCoordinates = (List<List<List<Double>>>) geometry.get("coordinates");

            if (allCoordinates == null || allCoordinates.isEmpty()) {
                continue;
            }


            List<List<Double>> outerRing = allCoordinates.get(0);

            if (isPointInsidePolygon(latitude, longitude, outerRing)) {
                return Map.of(
                        "status", "RESTRITO",
                        "message", "Esta área tem restrições para voos de drones.",
                        "zone", airspace.get("name"),
                        "country", airspace.get("country")
                );
            }
        }

        return Map.of(
                "status", "PERMITIDO",
                "message", "Esta área é segura para voos de drones."
        );
    }

    private boolean isPointInsidePolygon(double lat, double lon, List<List<Double>> polygon) {
        int intersections = 0;
        for (int i = 0; i < polygon.size(); i++) {
            List<Double> point1 = polygon.get(i);
            List<Double> point2 = polygon.get((i + 1) % polygon.size());

            if (rayCastIntersect(lat, lon, point1, point2)) {
                intersections++;
            }
        }
        return (intersections % 2) == 1;
    }

    private boolean rayCastIntersect(double lat, double lon, List<Double> point1, List<Double> point2) {
        double lat1 = point1.get(1);
        double lon1 = point1.get(0);
        double lat2 = point2.get(1);
        double lon2 = point2.get(0);

        if ((lat1 > lat) != (lat2 > lat)) {
            double intersection = (lon2 - lon1) * (lat - lat1) / (lat2 - lat1) + lon1;
            return lon < intersection;
        }
        return false;
    }
}
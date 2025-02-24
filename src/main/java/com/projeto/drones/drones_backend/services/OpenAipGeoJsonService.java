package com.projeto.drones.drones_backend.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class OpenAipGeoJsonService {

    public JSONObject formatToGeoJson(List<Map<String, Object>> airspaces) {
        JSONObject geoJson = new JSONObject();
        geoJson.put("type", "FeatureCollection");

        JSONArray features = new JSONArray();
        for (Map<String, Object> airspace : airspaces) {
            JSONObject feature = new JSONObject();
            feature.put("type", "Feature");

            JSONObject properties = new JSONObject();
            properties.put("name", airspace.get("name"));
            properties.put("country", airspace.get("country"));
            properties.put("type", airspace.get("type"));

            feature.put("properties", properties);

            JSONObject geometry = new JSONObject();
            geometry.put("type", "Polygon");

            JSONArray coordinates = new JSONArray();
            JSONArray outerRing = new JSONArray();
            List<List<Double>> coords = (List<List<Double>>) ((Map<String, Object>) airspace.get("geometry")).get("coordinates");

            for (List<Double> point : coords) {
                JSONArray coordinate = new JSONArray();
                coordinate.put(point.get(0)); // Longitude
                coordinate.put(point.get(1)); // Latitude
                outerRing.put(coordinate);
            }

            coordinates.put(outerRing);
            geometry.put("coordinates", coordinates);
            feature.put("geometry", geometry);

            features.put(feature);
        }

        geoJson.put("features", features);
        return geoJson;
    }
}

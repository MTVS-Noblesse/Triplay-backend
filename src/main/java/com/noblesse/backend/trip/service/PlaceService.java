package com.noblesse.backend.trip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
public class PlaceService {

    @Value("${google.maps.api-key}")
    private String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<Map<String, Object>> getGooglePlaceDetailData(@RequestParam("location") String location) {
        String findPlaceUrl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="
                + location + "&inputtype=textquery&fields=place_id&language=ko&key="
                + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        String findPlaceResponse = restTemplate.getForObject(findPlaceUrl, String.class);

        // place_id 추출
        String placeId = extractPlaceId(findPlaceResponse);

        if (placeId != null) {
            // Place Details API 호출하여 운영시간 등 세부 정보 가져오기
            String placeDetailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?place_id="
                    + placeId + "&fields=name,formatted_address,geometry,opening_hours,formatted_phone_number,website,rating,photos&language=ko&key="
                    + apiKey;

            String placeDetailsResponse = restTemplate.getForObject(placeDetailsUrl, String.class);

            try {
                Map<String, Object> placeDetailsJson = objectMapper.readValue(placeDetailsResponse, Map.class);
                return new ResponseEntity<>(placeDetailsJson, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(Map.of("error", "Failed to parse JSON"), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return new ResponseEntity<>(Map.of("error", "No place found."), HttpStatus.NOT_FOUND);
        }
    }

    private String extractPlaceId(String findPlaceResponse) {
        String placeIdMarker = "\"place_id\" : \"";
        int startIndex = findPlaceResponse.indexOf(placeIdMarker);
        if (startIndex != -1) {
            startIndex += placeIdMarker.length();
            int endIndex = findPlaceResponse.indexOf("\"", startIndex);
            return findPlaceResponse.substring(startIndex, endIndex);
        }
        return null;
    }

    public ResponseEntity<Map<String, Object>> getGooglePlaceData(@RequestParam("location") String location) {
        String findPlaceUrl = "https://maps.googleapis.com/maps/api/geocode/json" +
                "?address=" + location +
                "&key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        String findPlaceResponse = restTemplate.getForObject(findPlaceUrl, String.class);

        Map<String, Object> placeDetailsJson = null;
        try {
            placeDetailsJson = objectMapper.readValue(findPlaceResponse, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        
        return new ResponseEntity<>(placeDetailsJson, HttpStatus.OK);
    }
}

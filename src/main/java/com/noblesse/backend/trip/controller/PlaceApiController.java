package com.noblesse.backend.trip.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://localhost:5173")  // React 앱이 실행되는 도메인
public class PlaceApiController {

    @Value("${google.maps.api-key}")
    private String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/getGooglePlaceData")
    public ResponseEntity<Map<String, Object>> getPlaceData(@RequestParam("location") String location) {
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
}

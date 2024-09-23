package com.noblesse.backend.trip.controller;

import com.noblesse.backend.trip.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PlaceApiController {

    private final PlaceService placeService;

    @Autowired
    public PlaceApiController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/getGooglePlaceData")
    public ResponseEntity<Map<String,Object>> getGooglePlaceData(@RequestParam("location") String location) {
        return placeService.getGooglePlaceData(location);
    }

    @GetMapping("/getGooglePlaceDetailData")
    public ResponseEntity<Map<String, Object>> getGooglePlaceDetailData(@RequestParam("location") String location) {
        return placeService.getGooglePlaceDetailData(location);
    }

}

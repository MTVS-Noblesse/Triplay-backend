package com.noblesse.backend.trip.controller;

import com.noblesse.backend.trip.dto.TripRegisterRequestDTO;
import com.noblesse.backend.trip.dto.TripUpdateRequestDTO;
import com.noblesse.backend.trip.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public ResponseEntity<?> getTrips() {
        return ResponseEntity.ok(tripService.findAll());
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<?> findTripById(@PathVariable("tripId") Long tripId) {
        return ResponseEntity.ok(tripService.findTripById(tripId));
    }

    @PostMapping
    public ResponseEntity<?> registerTrip(@ModelAttribute TripRegisterRequestDTO tripRegisterRequestDTO) {
        tripService.registerNewTrip(tripRegisterRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{tripId}")
    public ResponseEntity<?> updateTripById(@ModelAttribute TripUpdateRequestDTO tripUpdateRequestDTO) {
        tripService.updateTrip(tripUpdateRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<?> deleteTripById(@PathVariable("tripId") Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok().build();
    }

}

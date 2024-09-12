package com.noblesse.backend.trip.controller;

import com.noblesse.backend.trip.dto.TripRegisterRequestDTO;
import com.noblesse.backend.trip.dto.TripUpdateRequestDTO;
import com.noblesse.backend.trip.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(summary = "전체 여행 목록 조회")
    @GetMapping
    public ResponseEntity<?> getTrips() {
        return ResponseEntity.ok(tripService.findAll());
    }

    @Operation(summary = "여행 상세 조회")
    @GetMapping("/{tripId}")
    public ResponseEntity<?> findTripById(@PathVariable("tripId") Long tripId) {
        return ResponseEntity.ok(tripService.findTripById(tripId));
    }

    @Operation(summary = "새 여행 일정 추가")
    @PostMapping
    public ResponseEntity<?> registerTrip(@RequestBody TripRegisterRequestDTO tripRegisterRequestDTO) {
        tripService.registerNewTrip(tripRegisterRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "여행 일정 수정")
    @PatchMapping("/{tripId}")
    public ResponseEntity<?> updateTripById(@RequestBody TripUpdateRequestDTO tripUpdateRequestDTO) {
        tripService.updateTrip(tripUpdateRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "여행 일정 삭제")
    @DeleteMapping("/{tripId}")
    public ResponseEntity<?> deleteTripById(@PathVariable("tripId") Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok().build();
    }

}

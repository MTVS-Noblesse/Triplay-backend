package com.noblesse.backend.trip.dto;

import com.noblesse.backend.trip.domain.Place;
import com.noblesse.backend.trip.domain.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {
    private Long tripId;
    private String tripTitle;
    private String tripParty;
    private LocalDate tripStartDate;
    private LocalDate tripEndDate;
    private List<Place> places = new ArrayList<>();

    public TripDTO(Trip trip) {
        this.tripId = trip.getTripId();
        this.tripTitle = trip.getTripTitle();
        this.tripParty = trip.getTripParty();
        this.tripStartDate = trip.getTripStartDate();
        this.tripEndDate = trip.getTripEndDate();
        this.places = trip.getPlaces();
    }
}

package com.noblesse.backend.trip.dto;

import com.noblesse.backend.trip.domain.TripDate;

import java.time.LocalDate;
import java.util.List;

public class TripDateDTO {
    private LocalDate tripStartDate;
    private LocalDate tripEndDate;
    private List<PlaceDTO> places;

    public TripDateDTO(){}

    public TripDateDTO(LocalDate tripStartDate, LocalDate tripEndDate, List<PlaceDTO> places) {
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.places = places;
    }

    public TripDateDTO(TripDate tripDate) {
        this.tripStartDate = tripDate.getTripStartDate();
    }

    public LocalDate getTripStartDate() {
        return tripStartDate;
    }

    public LocalDate getTripEndDate() {
        return tripEndDate;
    }

    public List<PlaceDTO> getPlaces() {
        return places;
    }

    @Override
    public String toString() {
        return "TripDateDTO{" +
                "tripStartDate=" + tripStartDate +
                ", tripEndDate=" + tripEndDate +
                ", places=" + places +
                '}';
    }
}

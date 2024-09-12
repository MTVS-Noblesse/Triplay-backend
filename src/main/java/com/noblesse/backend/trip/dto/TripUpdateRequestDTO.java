package com.noblesse.backend.trip.dto;

import java.time.LocalDate;
import java.util.List;

public class TripUpdateRequestDTO {

    private Long tripId;
    private String tripTitle;
    private String tripParty;
    private LocalDate tripStartDate;
    private LocalDate tripEndDate;
    private List<PlaceDTO> places;

    public TripUpdateRequestDTO(Long tripId, String tripTitle, String tripParty, LocalDate tripStartDate, LocalDate tripEndDate, List<PlaceDTO> places) {
        this.tripId = tripId;
        this.tripTitle = tripTitle;
        this.tripParty = tripParty;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.places = places;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public String getTripParty() {
        return tripParty;
    }

    public void setTripParty(String tripParty) {
        this.tripParty = tripParty;
    }

    public LocalDate getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(LocalDate tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public LocalDate getTripEndDate() {
        return tripEndDate;
    }

    public void setTripEndDate(LocalDate tripEndDate) {
        this.tripEndDate = tripEndDate;
    }

    public List<PlaceDTO> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceDTO> places) {
        this.places = places;
    }
}

package com.noblesse.backend.trip.dto;

import java.util.List;

public class TripUpdateRequestDTO {

    private Long tripId;
    private String tripTitle;
    private String tripParty;
    private List<TripDateDTO> tripDates;

    public TripUpdateRequestDTO() {}

    public TripUpdateRequestDTO(Long tripId, String tripTitle, String tripParty, List<TripDateDTO> tripDates) {
        this.tripId = tripId;
        this.tripTitle = tripTitle;
        this.tripParty = tripParty;
        this.tripDates = tripDates;
    }

    public Long getTripId() {
        return tripId;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public String getTripParty() {
        return tripParty;
    }

    public List<TripDateDTO> getTripDates() {
        return tripDates;
    }

    @Override
    public String toString() {
        return "TripUpdateRequestDTO{" +
                "tripId=" + tripId +
                ", tripTitle='" + tripTitle + '\'' +
                ", tripParty='" + tripParty + '\'' +
                ", tripDates=" + tripDates +
                '}';
    }
}

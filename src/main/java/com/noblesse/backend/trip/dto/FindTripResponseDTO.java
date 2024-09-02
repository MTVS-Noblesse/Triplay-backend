package com.noblesse.backend.trip.dto;

import java.util.List;

public class FindTripResponseDTO {

    private Long tripId;
    private String tripTitle;
    private String tripParty;
    private List<TripDateDTO> tripDates;

    public FindTripResponseDTO() {}

    public FindTripResponseDTO(Long tripId, String tripTitle, String tripParty, List<TripDateDTO> tripDates) {
        this.tripId = tripId;
        this.tripTitle = tripTitle;
        this.tripParty = tripParty;
        this.tripDates = tripDates;
    }

    @Override
    public String toString() {
        return "FindTripResponseDTO{" +
                "tripId=" + tripId +
                ", tripTitle='" + tripTitle + '\'' +
                ", tripParty='" + tripParty + '\'' +
                ", tripDates=" + tripDates +
                '}';
    }
}

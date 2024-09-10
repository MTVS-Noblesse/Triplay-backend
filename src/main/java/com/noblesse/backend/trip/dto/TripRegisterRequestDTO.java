package com.noblesse.backend.trip.dto;

import java.util.List;

public class TripRegisterRequestDTO {
    private String tripTitle;
    private String tripParty;
    private List<TripDateDTO> tripDates;

    public TripRegisterRequestDTO(){}

    public TripRegisterRequestDTO(String tripTitle, String tripParty, List<TripDateDTO> tripDates) {
        this.tripTitle = tripTitle;
        this.tripParty = tripParty;
        this.tripDates = tripDates;
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
        return "TripRegisterDTO{" +
                "tripTitle='" + tripTitle + '\'' +
                ", tripParty='" + tripParty + '\'' +
                ", tripDates=" + tripDates +
                '}';
    }
}

package com.noblesse.backend.trip.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "trip_title")
    private String tripTitle;

    @Column(name = "trip_party")
    private String tripParty;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripDate> tripDates = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Trip() {}

    public Trip(Long tripId, String tripTitle, String tripParty, List<TripDate> tripDates, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.tripId = tripId;
        this.tripTitle = tripTitle;
        this.tripParty = tripParty;
        this.tripDates = tripDates;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public void setTripParty(String tripParty) {
        this.tripParty = tripParty;
    }

    public void setTripDates(List<TripDate> tripDates) {
        this.tripDates = tripDates;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId=" + tripId +
                ", tripTitle='" + tripTitle + '\'' +
                ", tripParty='" + tripParty + '\'' +
                ", tripDates=" + tripDates +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

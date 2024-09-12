package com.noblesse.backend.trip.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "trip_title")
    private String tripTitle;

    @Column(name = "trip_party")
    private String tripParty;

    @Column(name = "trip_start_date")
    private LocalDate tripStartDate;

    @Column(name = "trip_end_date")
    private LocalDate tripEndDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trip_id")
    private List<Place> places = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Trip() {}

    public Trip(Long tripId, String tripTitle, String tripParty, LocalDate tripStartDate, LocalDate tripEndDate, List<Place> places) {
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

    public String getTripTitle() {
        return tripTitle;
    }

    public String getTripParty() {
        return tripParty;
    }

    public LocalDate getTripStartDate() {
        return tripStartDate;
    }

    public LocalDate getTripEndDate() {
        return tripEndDate;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public void setTripParty(String tripParty) {
        this.tripParty = tripParty;
    }

    public void setTripStartDate(LocalDate tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public void setTripEndDate(LocalDate tripEndDate) {
        this.tripEndDate = tripEndDate;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
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
                ", tripStartDate=" + tripStartDate +
                ", tripEndDate=" + tripEndDate +
                ", places=" + places +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

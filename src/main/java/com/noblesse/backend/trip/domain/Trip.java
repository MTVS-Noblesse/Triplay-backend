package com.noblesse.backend.trip.domain;

import com.noblesse.backend.BaseTimeEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip")
public class Trip extends BaseTimeEntity {

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

    @Column(name = "user_id")
    private Long userId;

    public Trip() {}

    public Trip(Long tripId, String tripTitle, String tripParty, LocalDate tripStartDate, LocalDate tripEndDate, List<Place> places, Long userId) {
        this.tripId = tripId;
        this.tripTitle = tripTitle;
        this.tripParty = tripParty;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.places = places;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
                ", userId=" + userId +
                '}';
    }
}

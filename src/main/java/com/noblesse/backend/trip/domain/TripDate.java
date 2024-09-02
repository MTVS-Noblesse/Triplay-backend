package com.noblesse.backend.trip.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_trip_date")
public class TripDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_date_id")
    private long tripDateId;

    @Column(name = "trip_start_date")
    private LocalDate tripStartDate;

    @Column(name = "trip_end_date")
    private LocalDate tripEndDate;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @OneToMany(mappedBy = "tripDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Place> places = new ArrayList<>();

    public TripDate() {}

    public TripDate(long tripDateId, LocalDate tripStartDate, LocalDate tripEndDate, Trip trip, List<Place> places) {
        this.tripDateId = tripDateId;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.trip = trip;
        this.places = places;
    }

    public long getTripDateId() {
        return tripDateId;
    }

    public LocalDate getTripStartDate() {
        return tripStartDate;
    }

    public LocalDate getTripEndDate() {
        return tripEndDate;
    }

    public Trip getTrip() {
        return trip;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setTripStartDate(LocalDate tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public void setTripEndDate(LocalDate tripEndDate) {
        this.tripEndDate = tripEndDate;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "TripDate{" +
                "tripDateId=" + tripDateId +
                ", tripStartDate=" + tripStartDate +
                ", tripEndDate=" + tripEndDate +
                ", trip=" + trip +
                ", places=" + places +
                '}';
    }

}

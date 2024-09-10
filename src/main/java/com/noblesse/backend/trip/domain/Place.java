package com.noblesse.backend.trip.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tbl_place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private long placeId;

    @Column(name = "place_title")
    private String placeTitle;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "open_hour")
    private String openHour;

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "place_order")
    private int placeOrder;

    @Column(name = "place_thumbnail")
    private String placeThumbnail;

    @ManyToOne
    @JoinColumn(name = "trip_date_id", nullable = false)
    private TripDate tripDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Place() {}

    public Place(long placeId, String placeTitle, String address, double latitude, double longitude, String openHour, LocalTime departureTime, LocalTime arrivalTime, int placeOrder, String placeThumbnail, TripDate tripDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.placeId = placeId;
        this.placeTitle = placeTitle;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openHour = openHour;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.placeOrder = placeOrder;
        this.placeThumbnail = placeThumbnail;
        this.tripDate = tripDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public void setPlaceTitle(String placeTitle) {
        this.placeTitle = placeTitle;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setPlaceOrder(int placeOrder) {
        this.placeOrder = placeOrder;
    }

    public void setPlaceThumbnail(String placeThumbnail) {
        this.placeThumbnail = placeThumbnail;
    }

    public void setTripDate(TripDate tripDate) {
        this.tripDate = tripDate;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId=" + placeId +
                ", placeTitle='" + placeTitle + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", openHour='" + openHour + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", placeOrder=" + placeOrder +
                ", placeThumbnail='" + placeThumbnail + '\'' +
                ", tripDate=" + tripDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}

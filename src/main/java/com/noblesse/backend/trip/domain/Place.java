package com.noblesse.backend.trip.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "place")
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

    @Column(name = "visit_day")
    private int visitDay;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Place() {}

    public Place(String placeTitle, String address, double latitude, double longitude, String openHour, LocalTime departureTime, LocalTime arrivalTime, int placeOrder, String placeThumbnail, int visitDay) {
        this.placeTitle = placeTitle;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openHour = openHour;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.placeOrder = placeOrder;
        this.placeThumbnail = placeThumbnail;
        this.visitDay = visitDay;
    }

    public long getPlaceId() {
        return placeId;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public void setPlaceTitle(String placeTitle) {
        this.placeTitle = placeTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(int placeOrder) {
        this.placeOrder = placeOrder;
    }

    public String getPlaceThumbnail() {
        return placeThumbnail;
    }

    public void setPlaceThumbnail(String placeThumbnail) {
        this.placeThumbnail = placeThumbnail;
    }

    public int getvisitDay() {
        return visitDay;
    }

    public void setvisitDay(int visitDay) {
        this.visitDay = visitDay;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
                ", visitDay=" + visitDay +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

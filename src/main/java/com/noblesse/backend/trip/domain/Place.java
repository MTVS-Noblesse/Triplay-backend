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

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "address")
    private String address;

    @Column(name = "lat")
    private double lat;

    @Column(name = "lng")
    private double lng;

    @Column(name = "open_data", columnDefinition = "json")
    private String openData;

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "idx")
    private int idx;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "plan_day")
    private int planDay;
    
    @Column(name = "phone_number")
    private String phoneNumber;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Place() {}

    public Place(String locationName, String address, double lat, double lng, String openData, LocalTime departureTime, LocalTime arrivalTime, int idx, String photoUrl, int planDay, String phoneNumber) {
        this.locationName = locationName;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.openData = openData;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.idx = idx;
        this.photoUrl = photoUrl;
        this.planDay = planDay;
        this.phoneNumber = phoneNumber;
    }

    public long getPlaceId() {
        return placeId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getOpenData() {
        return openData;
    }

    public void setOpenData(String openData) {
        this.openData = openData;
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

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getPlanDay() {
        return planDay;
    }

    public void setPlanDay(int planDay) {
        this.planDay = planDay;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId=" + placeId +
                ", locationName='" + locationName + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", openData='" + openData + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", idx=" + idx +
                ", photoUrl='" + photoUrl + '\'' +
                ", planDay=" + planDay +
                ", phoneNumber=" + phoneNumber +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

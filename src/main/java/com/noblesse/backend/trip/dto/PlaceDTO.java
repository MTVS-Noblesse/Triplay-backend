package com.noblesse.backend.trip.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PlaceDTO {

    private String locationName;
    private String address;
    private double lat;
    private double lng;
    private List<String> openData;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int idx;
    private String photoUrl;
    private int planDay;
    private String phoneNumber;

    public PlaceDTO() {}

    public PlaceDTO(String locationName, String address, double lat, double lng, List<String> openData, LocalTime departureTime, LocalTime arrivalTime, int idx, String photoUrl, int planDay, String phoneNumber) {
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

    public List<String> getOpenData() {
        return openData;
    }

    public void setOpenData(List<String> openData) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "PlaceDTO{" +
                "locationName='" + locationName + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", openData='" + openData + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", idx=" + idx +
                ", photoUrl='" + photoUrl + '\'' +
                ", planDay=" + planDay +
                '}';
    }
}

package com.noblesse.backend.trip.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class PlaceDTO {

    private String placeTitle;
    private String address;
    private double latitude;
    private double longitude;
    private String openHour;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int placeOrder;
    private String placeThumbnail;
    private int visitDay;

    public PlaceDTO() {}

    public PlaceDTO(String placeTitle, String address, double latitude, double longitude, String openHour, LocalTime departureTime, LocalTime arrivalTime, int placeOrder, String placeThumbnail, int visitDay) {
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

    @Override
    public String toString() {
        return "PlaceDTO{" +
                "placeTitle='" + placeTitle + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", openHour='" + openHour + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", placeOrder=" + placeOrder +
                ", placeThumbnail='" + placeThumbnail + '\'' +
                ", visitDay=" + visitDay +
                '}';
    }
}

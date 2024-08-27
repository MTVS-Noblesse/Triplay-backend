package com.noblesse.backend.trip.dto;

import java.time.LocalTime;

public class PlaceDTO {
    private String placeTitle;
    private String address;
    private String openHour;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int placeOrder;
    private String placeThumbnail;

    public PlaceDTO(){}

    public PlaceDTO(String placeTitle, String address, String openHour, LocalTime departureTime, LocalTime arrivalTime, int placeOrder, String placeThumbnail) {
        this.placeTitle = placeTitle;
        this.address = address;
        this.openHour = openHour;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.placeOrder = placeOrder;
        this.placeThumbnail = placeThumbnail;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public String getAddress() {
        return address;
    }

    public String getOpenHour() {
        return openHour;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public int getPlaceOrder() {
        return placeOrder;
    }

    public String getPlaceThumbnail() {
        return placeThumbnail;
    }

    @Override
    public String toString() {
        return "PlaceDTO{" +
                "placeTitle='" + placeTitle + '\'' +
                ", address='" + address + '\'' +
                ", openHour='" + openHour + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", placeOrder=" + placeOrder +
                ", placeThumbnail='" + placeThumbnail + '\'' +
                '}';
    }
}

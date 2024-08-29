package com.noblesse.backend.trip.service;

import com.noblesse.backend.trip.domain.Place;
import com.noblesse.backend.trip.domain.Trip;
import com.noblesse.backend.trip.domain.TripDate;
import com.noblesse.backend.trip.dto.TripRegisterRequestDTO;
import com.noblesse.backend.trip.dto.TripUpdateRequestDTO;
import com.noblesse.backend.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Transactional
    public Trip registerNewTrip(TripRegisterRequestDTO requestDTO) {
        Trip trip = new Trip();
        trip.setTripTitle(requestDTO.getTripTitle());
        trip.setTripParty(requestDTO.getTripParty());

        List<TripDate> tripDates = requestDTO.getTripDates().stream()
                .map(dto -> {
                    TripDate tripDate = new TripDate();
                    tripDate.setTripStartDate(dto.getTripStartDate());
                    tripDate.setTripEndDate(dto.getTripEndDate());
                    tripDate.setTrip(trip);

                    List<Place> places = dto.getPlaces().stream()
                            .map(placeDTO -> {
                                Place place = new Place();
                                place.setPlaceTitle(placeDTO.getPlaceTitle());
                                place.setAddress(placeDTO.getAddress());
                                place.setOpenHour(placeDTO.getOpenHour());
                                place.setDepartureTime(placeDTO.getDepartureTime());
                                place.setArrivalTime(placeDTO.getArrivalTime());
                                place.setPlaceOrder(placeDTO.getPlaceOrder());
                                place.setPlaceThumbnail(placeDTO.getPlaceThumbnail());
                                place.setTripDate(tripDate);
                                return place;
                            }).collect(Collectors.toList());

                    tripDate.setPlaces(places);
                    return tripDate;
                }).collect(Collectors.toList());

        trip.setTripDates(tripDates);

        return tripRepository.save(trip);
    }

    @Transactional
    public void updateTrip(TripUpdateRequestDTO updateDTO) {
        Trip trip = tripRepository.findById(updateDTO.getTripId())
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        trip.setTripTitle(updateDTO.getTripTitle());
        trip.setTripParty(updateDTO.getTripParty());

        trip.getTripDates().clear();

        List<TripDate> tripDates = updateDTO.getTripDates().stream()
                .map(dto -> {
                    TripDate tripDate = new TripDate();
                    tripDate.setTripStartDate(dto.getTripStartDate());
                    tripDate.setTripEndDate(dto.getTripEndDate());
                    tripDate.setTrip(trip);

                    List<Place> places = dto.getPlaces().stream()
                            .map(placeDTO -> {
                                Place place = new Place();
                                place.setPlaceTitle(placeDTO.getPlaceTitle());
                                place.setAddress(placeDTO.getAddress());
                                place.setOpenHour(placeDTO.getOpenHour());
                                place.setDepartureTime(placeDTO.getDepartureTime());
                                place.setArrivalTime(placeDTO.getArrivalTime());
                                place.setPlaceOrder(placeDTO.getPlaceOrder());
                                place.setPlaceThumbnail(placeDTO.getPlaceThumbnail());
                                place.setTripDate(tripDate);
                                return place;
                            }).collect(Collectors.toList());

                    tripDate.setPlaces(places);
                    return tripDate;
                }).collect(Collectors.toList());

        trip.setTripDates(tripDates);

        tripRepository.save(trip);
    }

    @Transactional(readOnly = true)
    public Trip findTripById(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }
}

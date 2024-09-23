package com.noblesse.backend.trip.service;

import com.noblesse.backend.trip.domain.Place;
import com.noblesse.backend.trip.domain.Trip;
import com.noblesse.backend.trip.dto.TripDTO;
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

    public TripDTO findTripById(Long tripId) {
        return tripRepository.findById(tripId)
                .map(TripDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    @Transactional
    public Trip registerNewTrip(TripRegisterRequestDTO requestDTO) {
        Trip trip = new Trip();
        trip.setTripTitle(requestDTO.getTripTitle());
        trip.setTripParty(requestDTO.getTripParty());
        trip.setTripStartDate(requestDTO.getTripStartDate());
        trip.setTripEndDate(requestDTO.getTripEndDate());

        List<Place> places = requestDTO.getPlaces().stream()
                .map(placeDTO -> {
                    Place place = new Place();
                    place.setPlaceTitle(placeDTO.getPlaceTitle());
                    place.setAddress(placeDTO.getAddress());
                    place.setLatitude(placeDTO.getLatitude());
                    place.setLongitude(placeDTO.getLongitude());
                    place.setOpenHour(placeDTO.getOpenHour());
                    place.setDepartureTime(placeDTO.getDepartureTime());
                    place.setArrivalTime(placeDTO.getArrivalTime());
                    place.setPlaceOrder(placeDTO.getPlaceOrder());
                    place.setPlaceThumbnail(placeDTO.getPlaceThumbnail());
                    place.setvisitDay(placeDTO.getvisitDay());
                    return place;
                }).collect(Collectors.toList());

        trip.setPlaces(places);

        return tripRepository.save(trip);
    }

    @Transactional
    public void updateTrip(TripUpdateRequestDTO updateDTO) {
        Trip trip = tripRepository.findById(updateDTO.getTripId())
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        trip.setTripTitle(updateDTO.getTripTitle());
        trip.setTripParty(updateDTO.getTripParty());
        trip.setTripStartDate(updateDTO.getTripStartDate());
        trip.setTripEndDate(updateDTO.getTripEndDate());

        trip.getPlaces().clear();

        List<Place> updatedPlaces = updateDTO.getPlaces().stream()
                .map(placeDTO -> {
                    Place place = new Place();
                    place.setPlaceTitle(placeDTO.getPlaceTitle());
                    place.setAddress(placeDTO.getAddress());
                    place.setLatitude(placeDTO.getLatitude());
                    place.setLongitude(placeDTO.getLongitude());
                    place.setOpenHour(placeDTO.getOpenHour());
                    place.setDepartureTime(placeDTO.getDepartureTime());
                    place.setArrivalTime(placeDTO.getArrivalTime());
                    place.setPlaceOrder(placeDTO.getPlaceOrder());
                    place.setPlaceThumbnail(placeDTO.getPlaceThumbnail());
                    place.setvisitDay(placeDTO.getvisitDay());
                    return place;
                }).collect(Collectors.toList());

        trip.getPlaces().addAll(updatedPlaces);

        tripRepository.save(trip);
    }


    @Transactional
    public void deleteTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip with ID " + tripId + " not found"));
        tripRepository.deleteById(tripId);
    }
}

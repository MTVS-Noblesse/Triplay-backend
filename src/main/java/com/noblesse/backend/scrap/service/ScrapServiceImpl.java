package com.noblesse.backend.scrap.service;

import com.noblesse.backend.oauth2.util.JwtUtil;
import com.noblesse.backend.trip.domain.Place;
import com.noblesse.backend.trip.domain.Trip;
import com.noblesse.backend.trip.dto.PlaceDTO;
import com.noblesse.backend.trip.dto.TripRegisterRequestDTO;
import com.noblesse.backend.trip.dto.TripUpdateRequestDTO;
import com.noblesse.backend.trip.service.PlaceService;
import com.noblesse.backend.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScrapServiceImpl implements ScrapService{

    private final JwtUtil jwtUtil;
    private final TripService tripService;

    @Autowired
    public ScrapServiceImpl(JwtUtil jwtUtil, TripService tripService) {
        this.jwtUtil = jwtUtil;
        this.tripService = tripService;
    }

    @Override
    public void scrapToMyExistingTrip(@RequestHeader("Authorization") String authorizationHeader, long myTripId, Place targetPlace) {
        String token = authorizationHeader.substring(7);
        Long myId = jwtUtil.extractUserId(token);

        // myId와 myTripId로 저장할 trip 객체 갖고오기.

        Trip myTrip = tripService.findTripById(myTripId);
        List<Place> myPlaceList = myTrip.getPlaces();
        List<PlaceDTO> newPlaceDTOList = new ArrayList<>();

        for(Place place : myPlaceList) {
            PlaceDTO placeDTO = new PlaceDTO(
                    place.getPlaceTitle(),
                    place.getAddress(),
                    place.getLatitude(),
                    place.getLongitude(),
                    place.getOpenHour(),
                    place.getDepartureTime(),
                    place.getArrivalTime(),
                    place.getPlaceOrder(),
                    place.getPlaceThumbnail(),
                    place.getvisitDay()
            );
            newPlaceDTOList.add(placeDTO);
        }

        newPlaceDTOList.add(new PlaceDTO(
                targetPlace.getPlaceTitle(),
                targetPlace.getAddress(),
                targetPlace.getLatitude(),
                targetPlace.getLongitude(),
                targetPlace.getOpenHour(),
                targetPlace.getDepartureTime(),
                targetPlace.getArrivalTime(),
                targetPlace.getPlaceOrder(),
                targetPlace.getPlaceThumbnail(),
                targetPlace.getvisitDay()
        ));

        tripService.updateTrip(new TripUpdateRequestDTO(
                myTrip.getTripId(),
                myTrip.getTripTitle(),
                myTrip.getTripParty(),
                myTrip.getTripStartDate(),
                myTrip.getTripEndDate(),
                newPlaceDTOList
        ));
    }
}

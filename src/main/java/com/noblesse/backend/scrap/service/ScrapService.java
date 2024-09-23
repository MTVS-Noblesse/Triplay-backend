package com.noblesse.backend.scrap.service;

import com.noblesse.backend.trip.domain.Place;

public interface ScrapService {
    void scrapToMyExistingTrip(String authorizationHeader, long myTripId, Place targetPlace);
}

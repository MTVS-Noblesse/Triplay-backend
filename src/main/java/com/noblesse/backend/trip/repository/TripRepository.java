package com.noblesse.backend.trip.repository;

import com.noblesse.backend.trip.domain.Trip;
import com.noblesse.backend.trip.dto.TripDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByUserId(Long userId);

    @Query("""
    SELECT t
    FROM Trip t
    WHERE t.userId = :userId
""")
    List<Trip> findMobileByUserId(@Param("userId") Long userId);
}

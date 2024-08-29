package com.noblesse.backend.trip;

import com.noblesse.backend.trip.domain.Trip;
import com.noblesse.backend.trip.domain.TripDate;
import com.noblesse.backend.trip.dto.*;
import com.noblesse.backend.trip.service.TripService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class TripCRUDTests {

    @Autowired
    private TripService tripService;

    private static Stream<Arguments> newTrip() {
        return Stream.of(
                Arguments.of(new TripRegisterRequestDTO(
                        "여름 휴가", "가족",
                        List.of(
                                new TripDateDTO(LocalDate.of(2024, 8, 10), LocalDate.of(2024, 8, 12),
                                        List.of(
                                                new PlaceDTO("Beach", "123 Ocean Drive", "09:00 - 18:00", LocalTime.of(10, 0), LocalTime.of(12, 0), 1, "1.jpg")
                                        )),
                                new TripDateDTO(LocalDate.of(2024, 8, 13), LocalDate.of(2024, 8, 15),
                                        List.of(
                                                new PlaceDTO("Mountain", "456 Hill Street", "08:00 - 16:00", LocalTime.of(9, 0), LocalTime.of(11, 0), 2,"2.jpg")
                                        ))
                        ))
                ),
                Arguments.of(new TripRegisterRequestDTO(
                        "Winter Adventure", "Friends",
                        List.of(
                                new TripDateDTO(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25),
                                        List.of(
                                                new PlaceDTO("Ski Resort", "789 Snowy Path", "08:00 - 20:00", LocalTime.of(8, 0), LocalTime.of(10, 0), 1, "1.jpg"),
                                                new PlaceDTO("Hot Springs", "101 Warm Way", "10:00 - 22:00", LocalTime.of(11, 0), LocalTime.of(12, 0), 2, "2.jpg")
                                        ))
                        ))
                )
        );
    }

    private Trip registerSampleTrip() {
        TripRegisterRequestDTO initialTrip = new TripRegisterRequestDTO(
                "여름 휴가", "가족",
                List.of(
                        new TripDateDTO(LocalDate.of(2024, 8, 10), LocalDate.of(2024, 8, 12),
                                List.of(
                                        new PlaceDTO("Beach", "123 Ocean Drive", "09:00 - 18:00", LocalTime.of(10, 0), LocalTime.of(12, 0), 1, "1.jpg")
                                )),
                        new TripDateDTO(LocalDate.of(2024, 8, 13), LocalDate.of(2024, 8, 15),
                                List.of(
                                        new PlaceDTO("Mountain", "456 Hill Street", "08:00 - 16:00", LocalTime.of(13, 0), LocalTime.of(14, 0), 2, "2.jpg")
                                ))
                )
        );
        return tripService.registerNewTrip(initialTrip);
    }

    @ParameterizedTest
    @MethodSource("newTrip")
    void testRegisterNewTrip(TripRegisterRequestDTO tripInfo){
        Assertions.assertDoesNotThrow(() -> tripService.registerNewTrip(tripInfo));
    }

    @Test
    @Transactional
    void testUpdateTrip() {
        TripRegisterRequestDTO initialTrip = new TripRegisterRequestDTO(
                "여름 휴가", "가족",
                List.of(
                        new TripDateDTO(LocalDate.of(2024, 8, 10), LocalDate.of(2024, 8, 12),
                                List.of(
                                        new PlaceDTO("Beach", "123 Ocean Drive", "09:00 - 18:00", LocalTime.of(10, 0), LocalTime.of(12, 0), 1, "1.jpg")
                                )),
                        new TripDateDTO(LocalDate.of(2024, 8, 13), LocalDate.of(2024, 8, 15),
                                List.of(
                                        new PlaceDTO("Mountain", "456 Hill Street", "08:00 - 16:00", LocalTime.of(13, 0), LocalTime.of(14, 0), 2, "2.jpg")
                                ))
                )
        );

        Trip savedTrip = tripService.registerNewTrip(initialTrip);

        TripUpdateRequestDTO updatedTrip = new TripUpdateRequestDTO(
                savedTrip.getTripId(),
                "가을 휴가", "친구들",
                List.of(
                        new TripDateDTO(LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 3),
                                List.of(
                                        new PlaceDTO("Forest", "789 Green Way", "07:00 - 18:00", LocalTime.of(8, 0), LocalTime.of(9, 30), 1, "forest.jpg")
                                )),
                        new TripDateDTO(LocalDate.of(2024, 10, 4), LocalDate.of(2024, 10, 6),
                                List.of(
                                        new PlaceDTO("Lake", "987 Water Road", "09:00 - 17:00", LocalTime.of(11, 30), LocalTime.of(13, 45), 2, "lake.jpg")
                                ))
                )
        );

        Assertions.assertDoesNotThrow(() -> tripService.updateTrip(updatedTrip));

        Trip updatedTripFromDB = tripService.findTripById(savedTrip.getTripId());

        Assertions.assertEquals("가을 휴가", updatedTripFromDB.getTripTitle());
        Assertions.assertEquals("친구들", updatedTripFromDB.getTripParty());

        List<TripDate> tripDates = updatedTripFromDB.getTripDates();
        Assertions.assertEquals(2, tripDates.size());

        TripDate firstDate = tripDates.get(0);
        Assertions.assertEquals(LocalDate.of(2024, 10, 1), firstDate.getTripStartDate());
        Assertions.assertEquals("Forest", firstDate.getPlaces().get(0).getPlaceTitle());

        TripDate secondDate = tripDates.get(1);
        Assertions.assertEquals(LocalDate.of(2024, 10, 4), secondDate.getTripStartDate());
        Assertions.assertEquals("Lake", secondDate.getPlaces().get(0).getPlaceTitle());
    }

}

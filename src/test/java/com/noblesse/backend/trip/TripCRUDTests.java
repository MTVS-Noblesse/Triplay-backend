package com.noblesse.backend.trip;

import com.noblesse.backend.trip.domain.Place;
import com.noblesse.backend.trip.domain.Trip;
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
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class TripCRUDTests {

    @Autowired
    private TripService tripService;

    private static Stream<Arguments> newTrip() {
        return Stream.of(
                Arguments.of(new TripRegisterRequestDTO(
                        "가을 휴가", "친구들",
                        LocalDate.of(2024, 10, 1),
                        LocalDate.of(2024, 10, 3),
                        List.of(
                                new PlaceDTO("경복궁", "서울 종로구 사직로 161", 37.579617, 126.977041, "09:00 - 18:00", LocalTime.of(10, 0), LocalTime.of(12, 0), 1, "gyeongbokgung.jpg", 1),
                                new PlaceDTO("남산타워", "서울 용산구 남산공원길 105", 37.551169, 126.988227, "10:00 - 22:00", LocalTime.of(13, 0), LocalTime.of(15, 0), 2, "namsan_tower.jpg", 1),
                                new PlaceDTO("청계천", "서울 종로구 서린동", 37.570431, 126.979373, "00:00 - 24:00", LocalTime.of(16, 0), LocalTime.of(18, 0), 3, "cheonggyecheon.jpg", 1),
                                new PlaceDTO("한라산", "제주특별자치도 제주시 1100로", 33.362500, 126.533694, "05:00 - 17:00", LocalTime.of(9, 0), LocalTime.of(11, 0), 1, "hallasan.jpg", 2),
                                new PlaceDTO("성산일출봉", "제주특별자치도 서귀포시 성산읍", 33.458331, 126.941439, "07:00 - 19:00", LocalTime.of(12, 0), LocalTime.of(14, 0), 2, "seongsan_ilchulbong.jpg", 2),
                                new PlaceDTO("정동진", "강원도 강릉시 강동면", 37.690916, 129.033684, "06:00 - 20:00", LocalTime.of(8, 0), LocalTime.of(10, 0), 3, "jeongdongjin.jpg", 2)
                        )
                ))
        );
    }


    private Trip registerSampleTrip() {
        TripRegisterRequestDTO initialTrip = new TripRegisterRequestDTO(
                "가을 휴가", "친구들",
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 3),
                List.of(
                        new PlaceDTO("경복궁", "서울 종로구 사직로 161", 37.579617, 126.977041, "09:00 - 18:00", LocalTime.of(10, 0), LocalTime.of(12, 0), 1, "gyeongbokgung.jpg", 1),
                        new PlaceDTO("남산타워", "서울 용산구 남산공원길 105", 37.551169, 126.988227, "10:00 - 22:00", LocalTime.of(13, 0), LocalTime.of(15, 0), 2, "namsan_tower.jpg", 1),
                        new PlaceDTO("청계천", "서울 종로구 서린동", 37.570431, 126.979373, "00:00 - 24:00", LocalTime.of(16, 0), LocalTime.of(18, 0), 3, "cheonggyecheon.jpg", 1)
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
                LocalDate.of(2024, 8, 10),
                LocalDate.of(2024, 8, 11),
                List.of(
                        new PlaceDTO("해운대 해수욕장", "부산광역시 해운대구 해운대해변로 264", 35.158698, 129.160384, "09:00 - 18:00", LocalTime.of(10, 0), LocalTime.of(12, 0), 1, "haeundae.jpg", 1),
                        new PlaceDTO("광안리 해수욕장", "부산광역시 수영구 광안해변로 219", 35.153237, 129.118527, "09:00 - 22:00", LocalTime.of(13, 0), LocalTime.of(15, 0), 2, "gwangalli.jpg", 1),
                        new PlaceDTO("태종대", "부산광역시 영도구 전망로 24", 35.051755, 129.084639, "09:00 - 18:00", LocalTime.of(16, 0), LocalTime.of(18, 0), 3, "taejongdae.jpg", 1)
                )
        );

        Trip savedTrip = tripService.registerNewTrip(initialTrip);

        TripUpdateRequestDTO updatedTrip = new TripUpdateRequestDTO(
                savedTrip.getTripId(),
                "겨울 여행", "가족",
                LocalDate.of(2024, 12, 1),
                LocalDate.of(2024, 12, 2),
                List.of(
                        new PlaceDTO("부산 해운대", "부산 해운대구 해운대해변로", 35.159804, 129.163985, "09:00 - 18:00", LocalTime.of(9, 0), LocalTime.of(11, 0), 1, "haeundae.jpg", 1),
                        new PlaceDTO("울산 대왕암", "울산 동구 일산동", 35.491758, 129.429664, "08:00 - 17:00", LocalTime.of(12, 0), LocalTime.of(14, 0), 2, "daewangam.jpg", 1)
                )
        );

        Assertions.assertDoesNotThrow(() -> tripService.updateTrip(updatedTrip));

        TripDTO updatedTripFromDB = tripService.findTripById(savedTrip.getTripId());

        Assertions.assertEquals("겨울 여행", updatedTripFromDB.getTripTitle());
        Assertions.assertEquals("가족", updatedTripFromDB.getTripParty());

        List<Place> places = updatedTripFromDB.getPlaces();
        Assertions.assertEquals(2, places.size());

        Place firstPlace = places.get(0);
        Assertions.assertEquals("부산 해운대", firstPlace.getPlaceTitle());
        Assertions.assertEquals(1, firstPlace.getvisitDay());

        Place secondPlace = places.get(1);
        Assertions.assertEquals("울산 대왕암", secondPlace.getPlaceTitle());
        Assertions.assertEquals(1, secondPlace.getvisitDay());
    }

    @Test
    @Transactional
    void testDeleteTrip() {
        Trip savedTrip = registerSampleTrip();
        Assertions.assertDoesNotThrow(() -> tripService.deleteTrip(savedTrip.getTripId()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> tripService.findTripById(savedTrip.getTripId()));
    }
}


package com.noblesse.backend.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripDateDTO {
    private LocalDate tripStartDate;
    private LocalDate tripEndDate;
    private List<PlaceDTO> places;
}

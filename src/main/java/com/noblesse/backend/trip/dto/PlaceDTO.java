package com.noblesse.backend.trip.dto;

import com.noblesse.backend.file.dto.FileDTO;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlaceDTO {
    private Long placeId;
    private String locationName;
    private String address;
    private double lat;
    private double lng;
    private List<String> openData;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int idx;
    private String photoUrl;
    private int planDay;
    private String phoneNumber;
    private List<FileDTO> files; // Place와 연결된 이미지 파일
}


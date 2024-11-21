package com.noblesse.backend.oauth2.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MobileMyPageDTO {
    private String userName;
    private String email;
    private String profileUrl;
}

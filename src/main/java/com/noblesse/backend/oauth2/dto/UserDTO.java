package com.noblesse.backend.oauth2.dto;

import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String userName;
    private String email;
    @Nullable
    private String profileUrl;
}


package com.noblesse.backend.manage.user.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserManagementDTO {
    private Long id;
    private String email;
    private String userName;
    private String provider;
    private LocalDateTime firedAt;
    private boolean isFired;
    private List<String> preferences;
}

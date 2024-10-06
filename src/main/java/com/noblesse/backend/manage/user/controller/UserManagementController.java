package com.noblesse.backend.manage.user.controller;

import com.noblesse.backend.manage.user.dto.UserManagementDTO;
import com.noblesse.backend.manage.user.dto.UserStatusUpdateDTO;
import com.noblesse.backend.manage.user.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/user")
@RequiredArgsConstructor
@Tag(name = "회원 관리", description = "회원 조회 및 상태 관리 API")
public class UserManagementController {

    private final UserManagementService userManagementService;

    /** 회원 관리 페이지 엔드포인트 */
    @Operation(summary = "모든 회원 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 회원 조회 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    // 모든 회원 목록 조회 + 페이지네이션
    public ResponseEntity<Page<UserManagementDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "firedAt") String sortBy) {
        return ResponseEntity.ok(userManagementService.getAllUsers(page, size, sortBy));
    }

    /** 특정 회원 상태 수정(정지/정지 해제) 엔드포인트 */
    @Operation(summary = "특정 회원 상태 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 상태 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{userId}/status")
    public ResponseEntity<UserManagementDTO> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody UserStatusUpdateDTO statusUpdateDTO) {
        UserManagementDTO updatedUser = userManagementService.updateUserStatus(userId, statusUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }
}

package com.noblesse.backend.manage.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noblesse.backend.admin.dto.AdminLoginRequest;
import com.noblesse.backend.manage.user.dto.UserStatusUpdateDTO;
import com.noblesse.backend.manage.user.service.UserManagementService;
import com.noblesse.backend.oauth2.entity.OAuthUser;
import com.noblesse.backend.oauth2.repository.OAuthRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserManagementTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private OAuthRepository oAuthRepository;

    private static final String ADMIN_EMAIL = "admin@triplay.com";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String TEST_USER_EMAIL = "dipleinelven@naver.com";

    private String adminToken;
    private Long testUserId;

    @BeforeAll
    public void setup() throws Exception {
        // 테스트 사용자 생성
        OAuthUser testUser = new OAuthUser();
        testUser.setEmail(TEST_USER_EMAIL);
        testUser.setUserName("Test User");
        testUser = oAuthRepository.save(testUser);
        testUserId = testUser.getId();

        // 관리자 로그인 및 토큰 획득
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(ADMIN_EMAIL, ADMIN_PASSWORD);
        MvcResult result = mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        adminToken = objectMapper.readTree(response).get("token").asText();
    }

    @AfterAll
    public void cleanup() {
        oAuthRepository.deleteById(testUserId);
    }

    @Test
    @Order(1)
    @DisplayName("#01. 회원 목록 조회 테스트")
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/admin/user")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[4].email").value(TEST_USER_EMAIL));
    }

    @Test
    @Order(2)
    @DisplayName("#02. 회원 정지 테스트")
    public void testSuspendUser() throws Exception {
        UserStatusUpdateDTO updateDTO = new UserStatusUpdateDTO();
        updateDTO.setFired(true);

        mockMvc.perform(post("/admin/user/" + testUserId + "/status")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fired").value(true))
                .andExpect(jsonPath("$.firedAt").isNotEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("#03. 회원 정지 해제 테스트")
    public void testReinstateUser() throws Exception {
        UserStatusUpdateDTO updateDTO = new UserStatusUpdateDTO();
        updateDTO.setFired(false);

        mockMvc.perform(post("/admin/user/" + testUserId + "/status")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fired").value(false))
                .andExpect(jsonPath("$.firedAt").isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("#04. 인증 없이 회원 관리 API 접근 테스트")
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/admin/user"))
                .andExpect(status().isUnauthorized());

        UserStatusUpdateDTO updateDTO = new UserStatusUpdateDTO();
        updateDTO.setFired(true);

        mockMvc.perform(post("/admin/user/" + testUserId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(5)
    @DisplayName("#05. 존재하지 않는 회원 정지 시도 테스트")
    public void testSuspendNonExistentUser() throws Exception {
        UserStatusUpdateDTO updateDTO = new UserStatusUpdateDTO();
        updateDTO.setFired(true);

        mockMvc.perform(post("/admin/user/99999/status")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }
}
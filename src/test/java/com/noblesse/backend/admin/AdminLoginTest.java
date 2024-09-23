package com.noblesse.backend.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noblesse.backend.admin.dto.AdminDTO;
import com.noblesse.backend.admin.dto.AdminLoginRequest;
import com.noblesse.backend.admin.entity.Admin;
import com.noblesse.backend.admin.repository.AdminRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String ADMIN_PASSWORD = "testpassword";

    @BeforeAll
    public void setup() {
        adminRepository.deleteAll();
        AdminDTO adminDTO = AdminDTO.builder()
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .role("ROLE_ADMIN")
                .build();
        Admin admin = Admin.from(adminDTO);
        adminRepository.save(admin);
    }

    @AfterAll
    public void cleanup() {
        adminRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("#01. 관리자 로그인 성공 테스트")
    public void testAdminLogin() throws Exception {
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(ADMIN_EMAIL, ADMIN_PASSWORD);

        MvcResult result = mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refresh").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    @Order(2)
    @DisplayName("#02. 관리자 로그인 및 리프레시 토큰 재발급 테스트")
    public void testRefreshToken() throws Exception {
        // 1. 먼저 로그인을 수행합니다.
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(ADMIN_EMAIL, ADMIN_PASSWORD);
        MvcResult loginResult = mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 2. 로그인 응답에서 리프레시 토큰을 추출합니다.
        String loginResponse = loginResult.getResponse().getContentAsString();
        String refreshToken = objectMapper.readTree(loginResponse).get("refresh").asText();

        // 3. 리프레시 토큰을 사용하여 새 토큰을 요청합니다.
        mockMvc.perform(post("/admin/refresh")
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refresh").exists());
    }

    @Test
    @Order(3)
    @DisplayName("#03. 관리자 로그인 실패 테스트")
    public void testInvalidLogin() throws Exception {
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(ADMIN_EMAIL, "wrongpassword");

        mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isBadRequest());
    }
}
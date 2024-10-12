package com.noblesse.backend.manage.clip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noblesse.backend.admin.dto.AdminLoginRequest;
import com.noblesse.backend.clip.domain.Clip;
import com.noblesse.backend.clip.domain.ClipReport;
import com.noblesse.backend.clip.dto.ClipRegistRequestDTO;
import com.noblesse.backend.clip.dto.ClipReportRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipReportService;
import com.noblesse.backend.clip.service.ClipService;
import com.noblesse.backend.oauth2.entity.OAuthUser;
import com.noblesse.backend.oauth2.repository.OAuthRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClipManagementTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClipService clipService;

    @Autowired
    private ClipReportService clipReportService;

    @Autowired
    private OAuthRepository oAuthRepository;

    private static final String ADMIN_EMAIL = "admin@triplay.com";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String TEST_USER_EMAIL = "testuser@example.com";

    private String adminToken;
    private Long testUserId;
    private Long testClipId;

    @BeforeAll
    public void setup() throws Exception {
        // 테스트 사용자 생성
        OAuthUser testUser = new OAuthUser();
        testUser.setEmail(TEST_USER_EMAIL);
        testUser.setUserName("Test User");
        testUser = oAuthRepository.save(testUser);
        testUserId = testUser.getId();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        // 테스트 클립 생성
        ClipRegistRequestDTO clipDTO = new ClipRegistRequestDTO();
        clipDTO.setClipTitle("Test Clip");
        clipDTO.setClipUrl("https://example.com/test-clip");
        clipDTO.setIsOpened(true);
        clipDTO.setUserId(testUserId);
        clipDTO.setTripId(null); // 또는 적절한 tripId 설정
        clipDTO.setFile(mockFile);

        clipService.insertClip(clipDTO);
        testClipId = clipService.findAll().get(0).getClipId(); // 방금 생성한 클립의 ID 가져오기

        // 테스트 클립 신고 생성
        ClipReportRegistRequestDTO reportDTO = new ClipReportRegistRequestDTO();
        reportDTO.setReportCategoryId(1L);
        reportDTO.setClipReportTitle("Test Report");
        reportDTO.setClipReportContent("Test report content");
        reportDTO.setUserId(testUserId);
        reportDTO.setClipId(testClipId);
        clipReportService.registClipReport(reportDTO);

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
        List<ClipReport> reports = clipReportService.findAll();
        if (!reports.isEmpty()) {
            clipReportService.deleteClipReportByClipReportId(reports.get(0).getClipReportId());
        }

        List<Clip> clips = clipService.findAll();
        if (!clips.isEmpty()) {
            clipService.deleteClipByClipId(clips.get(0).getClipId());
        }

        if (testUserId != null) {
            oAuthRepository.deleteById(testUserId);
        }
    }

    @DisplayName("#01. 신고된 클립 목록 조회 테스트")
    @Test
    @Order(1)
    public void testGetReportedClips() throws Exception {
        mockMvc.perform(get("/admin/clip/reported")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].clipIds[0]").value(testClipId))
                .andExpect(jsonPath("$.content[0].email").value(TEST_USER_EMAIL));
    }

    @DisplayName("#02. 신고된 클립 상세 정보 조회 테스트")
    @Test
    @Order(2)
    public void testGetReportedClipDetail() throws Exception {
        mockMvc.perform(get("/admin/clip/reported/" + testClipId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clipId").value(testClipId))
                .andExpect(jsonPath("$.clipTitle").value("Test Clip"))
                .andExpect(jsonPath("$.email").value(TEST_USER_EMAIL));
    }

    @DisplayName("#03. 클립 삭제 테스트")
    @Test
    @Order(3)
    public void testDeleteClip() throws Exception {
        mockMvc.perform(delete("/admin/clip/" + testClipId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        // 클립이 실제로 삭제되었는지 확인
        mockMvc.perform(get("/admin/clip/reported/" + testClipId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @DisplayName("#04. 인증 없이 클립 관리 API 접근 테스트")
    @Test
    @Order(4)
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/admin/clip/reported"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/admin/clip/" + testClipId))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("#05. 존재하지 않는 클립 삭제 시도 테스트")
    @Test
    @Order(5)
    public void testDeleteNonExistentClip() throws Exception {
        mockMvc.perform(delete("/admin/clip/99999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
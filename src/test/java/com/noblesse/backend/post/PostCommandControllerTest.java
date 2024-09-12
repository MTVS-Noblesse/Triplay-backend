package com.noblesse.backend.post;

import com.noblesse.backend.oauth2.util.JwtUtil;
import com.noblesse.backend.post.api.command.PostCommandController;
import com.noblesse.backend.post.command.application.handler.CreatePostCommandHandler;
import com.noblesse.backend.post.command.application.handler.DeletePostCommandHandler;
import com.noblesse.backend.post.command.application.handler.UpdatePostCommandHandler;
import com.noblesse.backend.post.common.dto.PostDTO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostCommandControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CreatePostCommandHandler createPostCommandHandler;

    @Mock
    private UpdatePostCommandHandler updatePostCommandHandler;

    @Mock
    private DeletePostCommandHandler deletePostCommandHandler;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private PostCommandController postCommandController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postCommandController).build();
    }

    @DisplayName("#1. 로그인한 사용자의 포스트 생성 테스트")
    @Test
    @Order(1)
    void createPostTest() throws Exception {
        // Given
        String token = "Bearer validToken";
        Long userId = 1L;
        when(jwtUtil.extractUserId(anyString())).thenReturn(userId);
        when(createPostCommandHandler.handle(any(PostDTO.class))).thenReturn(1L);

        // When & Then
        mockMvc.perform(post("/post")
                        .header("Authorization", token)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("postTitle", "Test Title")
                        .param("postContent", "Test Content"))
                .andExpect(status().isOk());

        verify(jwtUtil).extractUserId("validToken");
        verify(createPostCommandHandler).handle(any(PostDTO.class));
    }

    @DisplayName("#2. 로그인한 사용자의 포스트 수정 테스트")
    @Test
    @Order(2)
    void updatePostTest() throws Exception {
        // Given
        String token = "Bearer validToken";
        Long userId = 1L;
        Long postId = 1L;
        when(jwtUtil.extractUserId(anyString())).thenReturn(userId);

        MockMultipartFile file = new MockMultipartFile("newImages", "test.jpg", "image/jpeg", "test image content".getBytes());

        // When & Then
        mockMvc.perform(multipart("/post/{postId}", postId)
                        .file(file)
                        .header("Authorization", token)
                        .param("postTitle", "Updated Title")
                        .param("postContent", "Updated Content")
                        .param("isOpened", "true")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isNoContent());

        verify(jwtUtil).extractUserId("validToken");
        verify(updatePostCommandHandler).handle(any(PostDTO.class));
    }

    @DisplayName("#3. 로그인한 사용자의 포스트 삭제 테스트")
    @Test
    @Order(3)
    void deletePostTest() throws Exception {
        // Given
        String token = "Bearer validToken";
        Long userId = 1L;
        Long postId = 1L;
        when(jwtUtil.extractUserId(anyString())).thenReturn(userId);

        // When & Then
        mockMvc.perform(delete("/post/{postId}", postId)
                        .header("Authorization", token))
                .andExpect(status().isNoContent());

        verify(jwtUtil).extractUserId("validToken");
        verify(deletePostCommandHandler).handle(any(PostDTO.class));
    }
}

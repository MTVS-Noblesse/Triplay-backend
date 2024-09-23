package com.noblesse.backend.oauth2.filter;

import com.noblesse.backend.oauth2.service.OAuth2Service;
import com.noblesse.backend.oauth2.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final OAuth2Service oAuth2Service;
    private final JwtUtil jwtUtil;
    @Autowired
    public JwtFilter(OAuth2Service oAuth2Service, JwtUtil jwtUtil) {
        this.oAuth2Service = oAuth2Service;
        this.jwtUtil = jwtUtil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (request.getRequestURI().equals("/refresh") || request.getRequestURI().equals("/oauth2/callback") || request.getRequestURI().equals("/admin/login") || request.getRequestURI().equals("/admin/refresh")) {
            filterChain.doFilter(request, response);
            return; // 로그인 요청은 필터를 통과하게 함
        }
        if (isSwaggerRequest(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                if (jwtUtil.validateAccessToken(token)) {
                    Long userId = jwtUtil.extractUserId(token);
                    UserDetails userDetails = oAuth2Service.loadUser(userId);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // 토큰이 유효하지 않을 경우 AUTH_001 오류 코드와 함께 응답
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"code\": \"AUTH_001\", \"message\": \"Invalid JWT token\"}");
                    response.setContentType("application/json");
                    return;
                }
            } catch (Exception e) {
                // 예외 발생 시 AUTH_001 오류 코드와 함께 응답
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"code\": \"AUTH_001\", \"message\": \"Unauthorized: " + e.getMessage() + "\"}");
                response.setContentType("application/json");
                return;
            }
        } else {
            // Authorization 헤더가 없거나 잘못된 경우 AUTH_001 오류 코드와 함께 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\": \"AUTH_001\", \"message\": \"Authorization header is missing or invalid\"}");
            response.setContentType("application/json");
            return;
        }
        filterChain.doFilter(request, response);
    }
    private boolean isSwaggerRequest(String requestURI) {
        return requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/webjars/springfox-swagger-ui") ||
                requestURI.startsWith("/swagger-ui.html");
    }
}


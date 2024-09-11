package com.noblesse.backend.oauth2.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noblesse.backend.oauth2.dto.TokenDTO;
import com.noblesse.backend.oauth2.service.OAuth2Service;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2Filter extends OncePerRequestFilter {

    private final OAuth2Service oAuth2Service;

    @Autowired
    public OAuth2Filter(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().equals("/oauth2/callback")) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, String>>() {});

            String code = requestBody.get("code");
            String state = requestBody.get("state");
            if (code != null && state != null) {
                TokenDTO tokens = oAuth2Service.authenticateNaver(code, state);

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("token", tokens.getToken());
                jsonResponse.put("refresh", tokens.getRefresh());

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(jsonResponse.toString());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}

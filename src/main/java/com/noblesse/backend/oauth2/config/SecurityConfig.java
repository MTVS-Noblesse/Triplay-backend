package com.noblesse.backend.oauth2.config;

import com.noblesse.backend.oauth2.filter.JwtFilter;
import com.noblesse.backend.oauth2.filter.OAuth2Filter;
import com.noblesse.backend.oauth2.service.OAuth2Service;
import com.noblesse.backend.oauth2.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final OAuth2Service oAuth2Service;
    private final CorsConfigurationSource corsConfigurationSource; // CORS 설정 추가
    private final OAuth2Filter oAuth2Filter;
    @Autowired
    public SecurityConfig(OAuth2Service oAuth2Service, CorsConfigurationSource corsConfigurationSource, OAuth2Filter oAuth2Filter) {
        this.oAuth2Service = oAuth2Service;
        this.corsConfigurationSource = corsConfigurationSource; // CORS 설정 주입
        this.oAuth2Filter = oAuth2Filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // CORS 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/oauth2/callback", "/swagger-ui/**", "/v3/api-docs/**", "/refresh", "/admin/login", "/admin/refresh", "/api/**").permitAll() // 로그인 페이지 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2Service))
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/main", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")) // 로그아웃 후 리다이렉트 URL
                .addFilterBefore(oAuth2Filter, BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(oAuth2Service, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}


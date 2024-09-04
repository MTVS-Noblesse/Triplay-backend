package com.noblesse.backend.oauth2.service;
import com.noblesse.backend.oauth2.entity.OAuthUser;
import com.noblesse.backend.oauth2.repository.OAuthRepository;
import com.noblesse.backend.oauth2.security.PrincipalDetails;
import com.noblesse.backend.oauth2.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.Optional;

@Service
public class OAuth2Service extends DefaultOAuth2UserService {

    private final String clientId;

    private final String clientSecret; // 네이버 클라이언트 Secret

    @Autowired
    private OAuthRepository oAuthRepository; // OAuthUserRepository로 변경

    @Autowired
    private JwtUtil jwtUtil; // JwtUtil 주입

    @Autowired
    private RestTemplate restTemplate;

    public OAuth2Service(@Value("${spring.security.oauth2.client.registration.naver.client-id}") String clientId, @Value("${spring.security.oauth2.client.registration.naver.client-secret}") String clientSecret, OAuthRepository oAuthRepository, JwtUtil jwtUtil, RestTemplate restTemplate) {
        this.oAuthRepository = oAuthRepository;
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String authenticateNaver(String code, String state) {
        // 1. 네이버 API를 통해 액세스 토큰 요청
        String tokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id="
                + clientId + "&client_secret=" + clientSecret + "&code=" + code + "&state=" + state;
        ResponseEntity<Map> tokenResponse = restTemplate.getForEntity(tokenUrl, Map.class);

        // 액세스 토큰 가져오기
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2. 액세스 토큰을 사용하여 사용자 정보 요청
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Map.class);

        Map<String, Object> userInfo = (Map<String, Object>) userInfoResponse.getBody().get("response");

        // 3. 사용자 정보를 DB에 저장하거나 적절히 처리
        String email = (String) userInfo.get("email");
        String providerId = (String) userInfo.get("id"); // 네이버에서 제공하는 고유 ID
        String userName = (String) userInfo.get("name"); // 사용자 이름
        String provider = "naver";
        System.out.println(userInfo);
        Optional<OAuthUser> optionalOAuthUser = oAuthRepository.findByProviderIdAndProvider(providerId, provider);
        OAuthUser oAuthUser;
        if (optionalOAuthUser.isPresent()) {
            oAuthUser = optionalOAuthUser.get();
        } else {
            oAuthUser = new OAuthUser();
            oAuthUser.setProviderId(providerId);
            oAuthUser.setProvider(provider);
            oAuthUser.setEmail(email);
            oAuthUser.setUsername(userName);

            oAuthRepository.save(oAuthUser);
        }

        // 4. JWT 생성

        return jwtUtil.generateToken(oAuthUser.getId()); // 생성된 JWT 토큰 반환
    }
    public UserDetails loadUser(Long userId) {
        // 사용자 정보를 데이터베이스에서 조회
        Optional<OAuthUser> optionalOAuthUser = oAuthRepository.findById(userId);

        // 사용자가 존재하지 않으면 예외 발생
        if (optionalOAuthUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }

        OAuthUser oAuthUser = optionalOAuthUser.get();

        // PrincipalDetails 객체 생성 및 반환
        return new PrincipalDetails(oAuthUser);
    }

}

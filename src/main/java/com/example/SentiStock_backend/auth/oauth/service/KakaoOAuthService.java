package com.example.SentiStock_backend.auth.oauth.service;

import com.example.SentiStock_backend.auth.oauth.dto.KakaoTokenResponse;
import com.example.SentiStock_backend.auth.oauth.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-secret:}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    
    //카카오 Access Token 발급 요청  
    public KakaoTokenResponse getKakaoToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        if (clientSecret != null && !clientSecret.isEmpty()) {
            params.add("client_secret", clientSecret);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, KakaoTokenResponse.class);

        return response.getBody();
    }

    
    //카카오 Access Token으로 사용자 정보 요청
    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, KakaoUserInfoResponse.class);

        return response.getBody();
    }

    public KakaoUserInfoResponse getUserInfoByCode(String code) {
        KakaoTokenResponse token = getKakaoToken(code);
        return getUserInfo(token.getAccess_token());
    }
}

package com.allog.dallog.infrastructure.oauth.client;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.infrastructure.oauth.dto.GoogleTokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleOAuthClient implements OAuthClient {

    private static final String JWT_DELIMITER = "\\.";

    private final String clientId;
    private final String clientSecret;
    private final String grantType;
    private final String redirectUri;
    private final String tokenUri;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleOAuthClient(@Value("${oauth.google.client-id}") final String clientId,
                             @Value("${oauth.google.client-secret}") final String clientSecret,
                             @Value("oauth.google.grant-type") final String grantType,
                             @Value("${oauth.google.redirect-uri}") final String redirectUri,
                             @Value("${oauth.google.token-uri}") final String tokenUri,
                             final RestTemplateBuilder restTemplateBuilder, final ObjectMapper objectMapper) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public OAuthMember getOAuthMember(final String code) {
        GoogleTokenResponse googleTokenResponse = requestGoogleToken(code);
        String payload = getPayloadFrom(googleTokenResponse.getIdToken());
        String decodedPayload = decodeJwtPayload(payload);

        try {
            return generateOAuthMemberBy(decodedPayload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }

    private GoogleTokenResponse requestGoogleToken(final String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = generateRequestParams(code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return restTemplate.postForEntity(tokenUri, request, GoogleTokenResponse.class).getBody();
    }

    private MultiValueMap<String, String> generateRequestParams(final String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("grant_type", grantType);
        params.add("redirect_uri", redirectUri);
        return params;
    }

    private String getPayloadFrom(final String jwt) {
        return jwt.split(JWT_DELIMITER)[1];
    }

    private String decodeJwtPayload(final String payload) {
        return new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
    }

    private OAuthMember generateOAuthMemberBy(final String decodedIdToken) throws JsonProcessingException {
        Map<String, String> userInfo = objectMapper.readValue(decodedIdToken, HashMap.class);
        String email = userInfo.get("email");
        String displayName = userInfo.get("name");
        String profileImageUrl = userInfo.get("picture");

        return new OAuthMember(email, displayName, profileImageUrl);
    }
}

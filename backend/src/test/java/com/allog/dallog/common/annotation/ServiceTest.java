package com.allog.dallog.common.annotation;

import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.response.AccessAndRefreshTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ExternalApiConfig.class)
@ActiveProfiles("test")
@Transactional
public class ServiceTest {

    @Autowired
    private AuthService authService;

    protected Long toMemberId(final OAuthMember oAuthMember) {
        AccessAndRefreshTokenResponse response = authService.generateAccessAndRefreshToken(oAuthMember);
        return authService.extractMemberId(response.getRefreshToken());
    }
}

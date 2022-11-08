package com.allog.dallog.common.annotation;

import com.allog.dallog.common.DatabaseCleaner;
import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.domain.TokenRepository;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.response.AccessAndRefreshTokenResponse;
import com.allog.dallog.global.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import({ExternalApiConfig.class, JpaConfig.class})
@ActiveProfiles("test")
public class ServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
        tokenRepository.deleteAll();
    }

    protected Long toMemberId(final OAuthMember oAuthMember) {
        AccessAndRefreshTokenResponse response = authService.generateAccessAndRefreshToken(oAuthMember);
        return authService.extractMemberId(response.getRefreshToken());
    }
}

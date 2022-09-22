package com.allog.dallog.common.annotation;

import com.allog.dallog.common.DatabaseCleaner;
import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ExternalApiConfig.class)
@ActiveProfiles("test")
public class ServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    protected Long parseMemberId(final TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.generateToken(tokenRequest);
        return authService.extractMemberId(tokenResponse.getAccessToken());
    }
}

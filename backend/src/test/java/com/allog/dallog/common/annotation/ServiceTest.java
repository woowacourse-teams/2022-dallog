package com.allog.dallog.common.annotation;

import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static com.allog.dallog.common.Constants.티거_이름;
import static com.allog.dallog.common.Constants.티거_이메일;
import static com.allog.dallog.common.Constants.티거_프로필_URL;

import com.allog.dallog.auth.application.AuthService;
import com.allog.dallog.auth.domain.TokenRepository;
import com.allog.dallog.auth.dto.OAuthMember;
import com.allog.dallog.auth.dto.response.AccessAndRefreshTokenResponse;
import com.allog.dallog.common.DatabaseCleaner;
import com.allog.dallog.common.builder.BuilderSupporter;
import com.allog.dallog.common.builder.GivenBuilder;
import com.allog.dallog.common.config.ExternalApiConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ExternalApiConfig.class)
@ActiveProfiles("test")
public abstract class ServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private BuilderSupporter builderSupporter;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
        tokenRepository.deleteAll();
    }

    protected Long toMemberId(final OAuthMember oAuthMember) {
        AccessAndRefreshTokenResponse response = authService.generateAccessAndRefreshToken(oAuthMember);
        return authService.extractMemberId(response.getRefreshToken());
    }

    protected GivenBuilder 나인() {
        GivenBuilder 나인 = new GivenBuilder(builderSupporter);
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL);
        return 나인;
    }

    protected GivenBuilder 티거() {
        GivenBuilder 티거 = new GivenBuilder(builderSupporter);
        티거.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);
        return 티거;
    }
}

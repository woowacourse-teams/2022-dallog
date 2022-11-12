package com.allog.dallog.common.config;

import static com.allog.dallog.common.fixtures.AuthFixtures.더미_시크릿_키;

import com.allog.dallog.domain.auth.application.StubTokenProvider;
import com.allog.dallog.auth.application.TokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TokenConfig {

    @Bean
    public TokenProvider tokenProvider() {
        return new StubTokenProvider(더미_시크릿_키);
    }
}

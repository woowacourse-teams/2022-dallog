package com.allog.dallog.common.config;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.infrastructure.oauth.client.StubExternalCalendarClient;
import com.allog.dallog.infrastructure.oauth.client.StubOAuthClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ExternalApiConfig {

    // SpringBootTest 환경에서 OAuthClient 실제 객체 대신 테스트 더블을 빈으로 등록하기 위한 코드
    @Bean
    public OAuthClient oAuthClient() {
        return new StubOAuthClient();
    }

    @Bean
    public ExternalCalendarClient externalCalendarClient() {
        return new StubExternalCalendarClient();
    }
}

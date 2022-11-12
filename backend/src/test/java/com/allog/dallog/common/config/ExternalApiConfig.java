package com.allog.dallog.common.config;

import com.allog.dallog.auth.application.OAuthClient;
import com.allog.dallog.auth.application.OAuthUri;
import com.allog.dallog.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.infrastructure.oauth.client.StubExternalCalendarClient;
import com.allog.dallog.infrastructure.oauth.client.StubOAuthClient;
import com.allog.dallog.infrastructure.oauth.uri.StubOAuthUri;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ExternalApiConfig {

    @Bean
    public OAuthClient oAuthClient() {
        return new StubOAuthClient();
    }

    @Bean
    public ExternalCalendarClient externalCalendarClient() {
        return new StubExternalCalendarClient();
    }

    @Bean
    public OAuthUri oAuthUri() {
        return new StubOAuthUri();
    }
}

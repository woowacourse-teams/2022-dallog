package com.allog.dallog.infrastructure.report;

import com.allog.dallog.global.error.Reporter;
import com.allog.dallog.global.error.dto.ErrorReportRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiscordReporter implements Reporter {

    private final String webhookUri;
    private final RestTemplate restTemplate;

    public DiscordReporter(@Value("${report.webhook.discord}") final String webhookUri,
                           final RestTemplateBuilder restTemplateBuilder) {
        this.webhookUri = webhookUri;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public void report(final ErrorReportRequest errorReportRequest) {
        Map<String, String> request = new HashMap<>();
        request.put("content", errorReportRequest.getLogMessage());
        restTemplate.postForEntity(webhookUri, request, Void.class);
    }
}

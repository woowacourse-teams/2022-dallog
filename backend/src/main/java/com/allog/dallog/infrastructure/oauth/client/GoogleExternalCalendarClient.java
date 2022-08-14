package com.allog.dallog.infrastructure.oauth.client;

import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.infrastructure.oauth.dto.GoogleCalendarListResponse;
import com.allog.dallog.infrastructure.oauth.exception.OAuthException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleExternalCalendarClient implements ExternalCalendarClient {

    private static final String CALENDAR_LIST_REQUEST_URI = "https://www.googleapis.com/calendar/v3/users/me/calendarList";
    private static final String ACCEPT_HEADER_NAME = "Accept";

    private final RestTemplate restTemplate;

    public GoogleExternalCalendarClient(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<ExternalCalendar> getExternalCalendar(final String accessToken) {
        HttpEntity<Void> request = new HttpEntity<>(generateCalendarListRequestHeaders(accessToken));
        GoogleCalendarListResponse response = fetchGoogleCalendarList(request).getBody();

        return response.getItems()
                .stream()
                .map(item -> new ExternalCalendar(item.getId(), item.getSummary()))
                .collect(Collectors.toList());
    }

    private HttpHeaders generateCalendarListRequestHeaders(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set(ACCEPT_HEADER_NAME, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private ResponseEntity<GoogleCalendarListResponse> fetchGoogleCalendarList(final HttpEntity<Void> request) {
        try {
            return restTemplate.getForEntity(CALENDAR_LIST_REQUEST_URI, GoogleCalendarListResponse.class, request);
        } catch (RestClientException e) {
            throw new OAuthException(e);
        }
    }
}

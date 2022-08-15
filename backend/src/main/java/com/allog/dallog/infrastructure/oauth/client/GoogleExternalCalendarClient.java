package com.allog.dallog.infrastructure.oauth.client;

import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarSchedule;
import com.allog.dallog.infrastructure.oauth.dto.GoogleCalendarEventResponse;
import com.allog.dallog.infrastructure.oauth.dto.GoogleCalendarEventsResponse;
import com.allog.dallog.infrastructure.oauth.dto.GoogleCalendarListResponse;
import com.allog.dallog.infrastructure.oauth.exception.OAuthException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleExternalCalendarClient implements ExternalCalendarClient {

    private static final String CALENDAR_LIST_REQUEST_URI = "https://www.googleapis.com/calendar/v3/users/me/calendarList";
    private static final String CALENDAR_EVENTS_REQUEST_URI = "https://www.googleapis.com/calendar/v3/calendars/{calendarId}/events?singleEvents=true&timeMax={timeMax}&timeMin={timeMin}";
    private static final String ACCEPT_HEADER_NAME = "Accept";

    private final RestTemplate restTemplate;

    public GoogleExternalCalendarClient(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<ExternalCalendar> getExternalCalendars(final String accessToken) {
        HttpEntity<Void> request = new HttpEntity<>(generateCalendarRequestHeaders(accessToken));
        GoogleCalendarListResponse response = fetchGoogleCalendarList(request).getBody();

        return response.getItems()
                .stream()
                .map(item -> new ExternalCalendar(item.getId(), item.getSummary()))
                .collect(Collectors.toList());
    }

    private ResponseEntity<GoogleCalendarListResponse> fetchGoogleCalendarList(final HttpEntity<Void> request) {
        try {
            return restTemplate.getForEntity(CALENDAR_LIST_REQUEST_URI, GoogleCalendarListResponse.class, request);
        } catch (RestClientException e) {
            throw new OAuthException(e);
        }
    }

    @Override
    public List<ExternalCalendarSchedule> getExternalCalendarSchedules(final String accessToken,
                                                                       final String calendarId,
                                                                       final String startDateTime,
                                                                       final String endDateTime) {
        HttpEntity<Void> request = new HttpEntity<>(generateCalendarRequestHeaders(accessToken));

        Map<String, String> uriVariables = generateEventsVariables(calendarId, startDateTime, endDateTime);
        GoogleCalendarEventsResponse response = fetchGoogleCalendarEvents(uriVariables, request).getBody();

        return response.getItems()
                .stream()
                .map(this::parseExternalResponse)
                .collect(Collectors.toList());
    }

    private HttpHeaders generateCalendarRequestHeaders(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set(ACCEPT_HEADER_NAME, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private Map<String, String> generateEventsVariables(final String externalCalendarId, final String startDateTime,
                                                        final String endDateTime) {
        return Map.of(
                "calendarId", externalCalendarId,
                "timeMax", endDateTime + "Z",
                "timeMin", startDateTime + "Z"
        );
    }

    private ResponseEntity<GoogleCalendarEventsResponse> fetchGoogleCalendarEvents(
            final Map<String, String> uriVariables, final HttpEntity<Void> request) {
        try {
            return restTemplate.exchange(CALENDAR_EVENTS_REQUEST_URI, HttpMethod.GET, request,
                    GoogleCalendarEventsResponse.class, uriVariables);
        } catch (RestClientException e) {
            throw new OAuthException(e);
        }
    }

    private ExternalCalendarSchedule parseExternalResponse(final GoogleCalendarEventResponse item) {
        return new ExternalCalendarSchedule(item.getId(), item.getSummary(), item.getDescription(),
                item.getStartDateTime(), item.getEndDateTime());
    }
}

package com.allog.dallog.presentation;

import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarService;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarsResponse;
import com.allog.dallog.presentation.auth.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalCalendarController {

    private final ExternalCalendarService externalCalendarService;

    public ExternalCalendarController(final ExternalCalendarService externalCalendarService) {
        this.externalCalendarService = externalCalendarService;
    }

    @GetMapping("/api/external-calendars/me")
    public ResponseEntity<ExternalCalendarsResponse> getExternalCalendar(
            @AuthenticationPrincipal final LoginMember loginMember) {

        return ResponseEntity.ok(externalCalendarService.findByMemberId(loginMember.getId()));
    }
}

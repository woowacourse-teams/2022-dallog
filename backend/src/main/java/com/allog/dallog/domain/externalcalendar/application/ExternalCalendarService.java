package com.allog.dallog.domain.externalcalendar.application;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.exception.NoSuchOAuthTokenException;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarsResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ExternalCalendarService {

    private final OAuthClient oAuthClient;
    private final ExternalCalendarClient externalCalendarClient;
    private final OAuthTokenRepository oAuthTokenRepository;

    public ExternalCalendarService(final OAuthClient oAuthClient, final ExternalCalendarClient externalCalendarClient,
                                   final OAuthTokenRepository oAuthTokenRepository) {
        this.oAuthClient = oAuthClient;
        this.externalCalendarClient = externalCalendarClient;
        this.oAuthTokenRepository = oAuthTokenRepository;
    }

    public ExternalCalendarsResponse findByMemberId(final Long memberId) {
        OAuthToken oAuthToken = oAuthTokenRepository.findByMemberId(memberId)
                .orElseThrow(NoSuchOAuthTokenException::new);

        String oAuthAccessToken = oAuthClient.getAccessToken(oAuthToken.getRefreshToken())
                .getValue();

        List<ExternalCalendar> externalCalendars = externalCalendarClient.getExternalCalendars(oAuthAccessToken);

        return new ExternalCalendarsResponse(externalCalendars);
    }
}

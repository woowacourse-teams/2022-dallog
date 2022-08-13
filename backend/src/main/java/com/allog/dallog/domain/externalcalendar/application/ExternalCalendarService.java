package com.allog.dallog.domain.externalcalendar.application;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.dto.response.OAuthAccessTokenResponse;
import com.allog.dallog.domain.auth.exception.NoSuchOAuthTokenException;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ExternalCalendarService {

    private final OAuthClient oAuthClient;
    private final CalendarClient calendarClient;
    private final OAuthTokenRepository oAuthTokenRepository;

    public ExternalCalendarService(final OAuthClient oAuthClient, final CalendarClient calendarClient,
                                   final OAuthTokenRepository oAuthTokenRepository) {
        this.oAuthClient = oAuthClient;
        this.calendarClient = calendarClient;
        this.oAuthTokenRepository = oAuthTokenRepository;
    }

    public List<ExternalCalendar> findByMemberId(final Long memberId) {
        OAuthToken oAuthToken = oAuthTokenRepository.findByMemberId(memberId)
                .orElseThrow(NoSuchOAuthTokenException::new);

        OAuthAccessTokenResponse oAuthAccessTokenResponse = oAuthClient.geAccessToken(oAuthToken.getRefreshToken());
        return calendarClient.getExternalCalendar(oAuthAccessTokenResponse.getAccessToken());
    }
}

package com.allog.dallog.externalcalendar.application;

import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.auth.domain.OAuthTokenRepository;
import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.builder.GivenBuilder;
import com.allog.dallog.externalcalendar.dto.ExternalCalendarsResponse;
import com.allog.dallog.member.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExternalCalendarServiceTest extends ServiceTest {

    @Autowired
    private ExternalCalendarService externalCalendarService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Test
    void 회원의_외부_캘린더_목록을_조회한다() {
        // given
        GivenBuilder 나인 = 나인();

        // when
        ExternalCalendarsResponse actual = externalCalendarService.findByMemberId(나인.회원().getId());

        // then
        assertThat(actual.getExternalCalendars()).hasSize(3);
    }
}

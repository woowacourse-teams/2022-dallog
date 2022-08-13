package com.allog.dallog.domain.externalcalendar.application;

import static com.allog.dallog.common.fixtures.OAuthTokenFixtures.OAUTH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.fixtures.MemberFixtures;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarsResponse;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExternalCalendarServiceTest extends ServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Autowired
    private ExternalCalendarService externalCalendarService;

    @DisplayName("member id를 활용하여 외부 캘린더 리스트를 조회한다.")
    @Test
    void member_id를_활용하여_외부_캘린더_리스트를_조회한다() {
        // given
        Member 매트 = memberRepository.save(MemberFixtures.매트());
        oAuthTokenRepository.save(OAUTH_TOKEN(매트));

        // when
        ExternalCalendarsResponse actual = externalCalendarService.findByMemberId(매트.getId());

        // then
        assertThat(actual.getExternalCalendars()).hasSize(3);
    }
}

package com.allog.dallog.domain.externalcalendar.application;

import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static com.allog.dallog.common.fixtures.OAuthTokenFixtures.OAUTH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarsResponse;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExternalCalendarServiceTest extends ServiceTest {

    @Autowired
    private ExternalCalendarService externalCalendarService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    private IntegrationLogicBuilder 나인;

    @BeforeEach
    void setUp() {
        나인 = new IntegrationLogicBuilder();
    }

    @Test
    void 회원의_외부_캘린더_목록을_조회한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL);

        // when
        ExternalCalendarsResponse actual = externalCalendarService.findByMemberId(나인.회원().getId());

        // then
        assertThat(actual.getExternalCalendars()).hasSize(3);
    }

    private final class IntegrationLogicBuilder {

        private Member member;

        private IntegrationLogicBuilder 회원_가입을_한다(final String email, final String name, final String profile) {
            this.member = new Member(email, name, profile, SocialType.GOOGLE);
            memberRepository.save(member);
            oAuthTokenRepository.save(OAUTH_TOKEN(member));
            return this;
        }

        private Member 회원() {
            return member;
        }
    }
}

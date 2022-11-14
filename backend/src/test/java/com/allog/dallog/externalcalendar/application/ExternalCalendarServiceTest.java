package com.allog.dallog.externalcalendar.application;

import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static com.allog.dallog.common.fixtures.OAuthTokenFixtures.OAUTH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.auth.domain.OAuthTokenRepository;
import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.externalcalendar.dto.ExternalCalendarsResponse;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.member.domain.SocialType;
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
        GivenBuilder 나인 = 나인().회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL);

        // when
        ExternalCalendarsResponse actual = externalCalendarService.findByMemberId(나인.회원().getId());

        // then
        assertThat(actual.getExternalCalendars()).hasSize(3);
    }

    private final class GivenBuilder {

        private Member member;

        private GivenBuilder 회원_가입을_한다(final String email, final String name, final String profile) {
            Member member = new Member(email, name, profile, SocialType.GOOGLE);
            this.member = memberRepository.save(member);
            oAuthTokenRepository.save(OAUTH_TOKEN(member));
            return this;
        }

        private Member 회원() {
            return member;
        }
    }

    private GivenBuilder 나인() {
        return new GivenBuilder();
    }
}

package com.allog.dallog.domain.externalcalendar.application;

import static com.allog.dallog.common.Constants.네오_이름;
import static com.allog.dallog.common.Constants.네오_이메일;
import static com.allog.dallog.common.Constants.네오_프로필_URL;
import static com.allog.dallog.common.fixtures.OAuthTokenFixtures.OAUTH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.IntegrationTest;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarsResponse;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExternalCalendarServiceTest extends IntegrationTest {

    @Autowired
    private ExternalCalendarService externalCalendarService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    private User 네오;

    @BeforeEach
    void setUp() {
        네오 = new User();
    }

    @DisplayName("member id를 활용하여 외부 캘린더 리스트를 조회한다.")
    @Test
    void member_id를_활용하여_외부_캘린더_리스트를_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        oAuthTokenRepository.save(OAUTH_TOKEN(네오.계정()));

        // when
        ExternalCalendarsResponse actual = externalCalendarService.findByMemberId(네오.계정().getId());

        // then
        assertThat(actual.getExternalCalendars()).hasSize(3);
    }

    private final class User {

        private Member member;

        private User 회원_가입을_한다(final String email, final String name, final String profile) {
            this.member = new Member(email, name, profile, SocialType.GOOGLE);
            memberRepository.save(member);
            return this;
        }

        private Member 계정() {
            return member;
        }
    }
}

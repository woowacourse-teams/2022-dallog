package com.allog.dallog.domain.auth.domain;

import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.OAuthTokenFixtures.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.auth.domain.OAuthToken;
import com.allog.dallog.auth.domain.OAuthTokenRepository;
import com.allog.dallog.common.annotation.RepositoryTest;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OAuthTokenRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @DisplayName("member id의 OAuthToken이 존재할 경우 true를 반환한다.")
    @Test
    void member_id의_OAuthToken이_존재할_경우_true를_반환한다() {
        // given
        Member 매트 = memberRepository.save(매트());
        oAuthTokenRepository.save(new OAuthToken(매트, REFRESH_TOKEN));

        // when
        boolean actual = oAuthTokenRepository.existsByMemberId(매트.getId());

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("member id의 OAuthToken이 존재하지 않을 경우 false를 반환한다.")
    @Test
    void member_id의_OAuthToken이_존재하지_않을_경우_false를_반환한다() {
        // given & when
        boolean actual = oAuthTokenRepository.existsByMemberId(0L);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("member id의 OAuthToken이 존재할 경우 Optional은 비어있지 않다.")
    @Test
    void member_id의_OAuthToken이_존재할_경우_Optional은_비어있지_않다() {
        // given
        Member 매트 = memberRepository.save(매트());
        oAuthTokenRepository.save(new OAuthToken(매트, REFRESH_TOKEN));

        // when
        Optional<OAuthToken> actual = oAuthTokenRepository.findByMemberId(매트.getId());

        // then
        assertThat(actual).isNotEmpty();
    }

    @DisplayName("member id의 OAuthToken이 존재하지 않을 경우 비어있다.")
    @Test
    void member_id의_OAuthToken이_존재하지_않을_경우_비어있다() {
        // given & when
        Optional<OAuthToken> actual = oAuthTokenRepository.findByMemberId(0L);

        // then
        assertThat(actual).isEmpty();
    }
}

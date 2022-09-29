package com.allog.dallog.domain.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryTokenRepositoryTest {

    @AfterEach
    void setUp() {
        InMemoryTokenRepository.deleteAll();
    }

    @DisplayName("토큰을 저장한다.")
    @Test
    void 토큰을_저장한다() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";

        // when
        InMemoryTokenRepository.save(dummyMemberId, dummyRefreshToken);

        // then
        assertThat(InMemoryTokenRepository.getToken(dummyMemberId)).isEqualTo(dummyRefreshToken);
    }

    @DisplayName("MemberId에 해당하는 토큰이 있으면 true를 반환한다.")
    @Test
    void MemberId에_해당하는_토큰이_있으면_true를_반환한다() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";
        InMemoryTokenRepository.save(dummyMemberId, dummyRefreshToken);

        // when
        boolean actual = InMemoryTokenRepository.exist(dummyMemberId);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("MemberId에 해당하는 토큰이 없으면 false를 반환한다.")
    @Test
    void MemberId에_해당하는_토큰이_없으면_false를_반환한다() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";

        // when
        boolean actual = InMemoryTokenRepository.exist(dummyMemberId);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("MemberId에 해당하는 토큰을 가져온다.")
    @Test
    void MemberId에_해당하는_토큰을_가져온다() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";
        InMemoryTokenRepository.save(dummyMemberId, dummyRefreshToken);

        // when
        String actual = InMemoryTokenRepository.getToken(dummyMemberId);

        // then
        assertThat(actual).isEqualTo(dummyRefreshToken);
    }
}

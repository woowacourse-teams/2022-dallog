package com.allog.dallog.domain.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.domain.auth.exception.NoSuchTokenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ExternalApiConfig.class)
@ActiveProfiles("test")
class InMemoryAuthTokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        tokenRepository.deleteAll();
    }

    @DisplayName("토큰을 저장한다.")
    @Test
    void 토큰을_저장한다() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";

        // when
        tokenRepository.save(dummyMemberId, dummyRefreshToken);

        // then
        assertThat(tokenRepository.getToken(dummyMemberId)).isEqualTo(dummyRefreshToken);
    }

    @DisplayName("MemberId에 해당하는 토큰이 있으면 true를 반환한다.")
    @Test
    void MemberId에_해당하는_토큰이_있으면_true를_반환한다() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";
        tokenRepository.save(dummyMemberId, dummyRefreshToken);

        // when
        boolean actual = tokenRepository.exist(dummyMemberId);

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
        boolean actual = tokenRepository.exist(dummyMemberId);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("MemberId에 해당하는 토큰을 가져온다.")
    @Test
    void MemberId에_해당하는_토큰을_가져온다() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";
        tokenRepository.save(dummyMemberId, dummyRefreshToken);

        // when
        String actual = tokenRepository.getToken(dummyMemberId);

        // then
        assertThat(actual).isEqualTo(dummyRefreshToken);
    }

    @DisplayName("MemberId에 해당하는 토큰이 없으면 예외를 발생한다.")
    @Test
    void MemberId에_해당하는_토큰이_없으면_예외를_발생한다() {
        // given
        Long dummyMemberId = 1L;

        // when & then
        assertThatThrownBy(() -> tokenRepository.getToken(dummyMemberId))
                .isInstanceOf(NoSuchTokenException.class);
    }
}

package com.allog.dallog.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.auth.domain.AuthToken;
import com.allog.dallog.common.annotation.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthTokenCreatorTest extends ServiceTest {

    @Autowired
    private TokenCreator tokenCreator;

    @DisplayName("엑세스 토큰과 리프레시 토큰을 발급한다.")
    @Test
    void 엑세스_토큰과_리프레시_토큰을_발급한다() {
        // given
        Long memberId = 1L;

        // when
        AuthToken authToken = tokenCreator.createAuthToken(memberId);

        // then
        assertThat(authToken.getAccessToken()).isNotEmpty();
        assertThat(authToken.getRefreshToken()).isNotEmpty();
    }

    @DisplayName("리프레시 토큰으로 엑세스 토큰을 발급한다.")
    @Test
    void 리프레시_토큰으로_엑세스_토큰을_발급한다() {
        // given
        Long memberId = 1L;
        AuthToken authToken = tokenCreator.createAuthToken(memberId);

        // when
        AuthToken actual = tokenCreator.renewAuthToken(authToken.getRefreshToken());

        // then
        assertThat(actual.getAccessToken()).isNotEmpty();
        assertThat(actual.getRefreshToken()).isNotEmpty();
    }

    @DisplayName("토큰에서 페이로드를 추출한다.")
    @Test
    void 토큰에서_페이로드를_추출한다() {
        // given
        Long memberId = 1L;
        AuthToken authToken = tokenCreator.createAuthToken(memberId);

        // when
        Long actual = tokenCreator.extractPayload(authToken.getAccessToken());

        // then
        assertThat(actual).isEqualTo(memberId);
    }
}

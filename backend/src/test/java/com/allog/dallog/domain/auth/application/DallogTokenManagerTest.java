package com.allog.dallog.domain.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.domain.DallogToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DallogTokenManagerTest extends ServiceTest {

    @Autowired
    private TokenManager tokenManager;

    @DisplayName("엑세스 토큰과 리프레시 토큰을 발급한다.")
    @Test
    void 엑세스_토큰과_리프레시_토큰을_발급한다() {
        // given
        Long memberId = 1L;

        // when
        DallogToken dallogToken = tokenManager.createDallogToken(memberId);

        // then
        assertThat(dallogToken.getAccessToken()).isNotEmpty();
        assertThat(dallogToken.getRefreshToken()).isNotEmpty();
    }

    @DisplayName("리프레시 토큰으로 엑세스 토큰을 발급한다.")
    @Test
    void 리프레시_토큰으로_엑세스_토큰을_발급한다() {
        // given
        Long memberId = 1L;
        DallogToken dallogToken = tokenManager.createDallogToken(memberId);

        // when
        DallogToken actual = tokenManager.renewDallogToken(dallogToken.getRefreshToken());

        // then
        assertThat(actual.getAccessToken()).isNotEmpty();
        assertThat(actual.getRefreshToken()).isNotEmpty();
    }

    @DisplayName("토큰에서 페이로드를 추출한다.")
    @Test
    void 토큰에서_페이로드를_추출한다() {
        // given
        Long memberId = 1L;
        DallogToken dallogToken = tokenManager.createDallogToken(memberId);

        // when
        Long actual = tokenManager.extractPayload(dallogToken.getAccessToken());

        // then
        assertThat(actual).isEqualTo(memberId);
    }
}

package com.allog.dallog.domain.auth.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.domain.auth.exception.NoSuchTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DallogTokenTest {

    @DisplayName("같은 리프레시 토큰 값이면 정상적으로 메서드를 종료한다.")
    @Test
    void 같은_리프레시_토큰_값이면_정상적으로_메서드를_종료한다() {
        // given
        DallogToken dallogToken = new DallogToken("dummyAccessToken", "dummyRefreshToken");

        // when & then
        dallogToken.validateHasSameRefreshToken(dallogToken.getRefreshToken());
    }

    @DisplayName("같은 리프레시 토큰 값이 아니면 예외를 발생한다.")
    @Test
    void 같은_리프레시_토큰_값이_아니면_예외를_발생한다() {
        // given
        DallogToken dallogToken = new DallogToken("dummyAccessToken", "dummyRefreshToken");

        // when & then
        assertThatThrownBy(() -> dallogToken.validateHasSameRefreshToken("invalidRefreshToken"))
                .isInstanceOf(NoSuchTokenException.class);
    }

}

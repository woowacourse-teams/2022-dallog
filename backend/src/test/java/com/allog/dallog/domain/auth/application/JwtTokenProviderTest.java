package com.allog.dallog.domain.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.domain.auth.exception.InvalidTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private static final String JWT_SECRET_KEY = "A".repeat(32); // Secret Key는 최소 32바이트 이상이어야함.
    private static final int JWT_EXPIRE_LENGTH = 3600;
    private static final String PAYLOAD = "payload";

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(JWT_SECRET_KEY, JWT_EXPIRE_LENGTH);

    @DisplayName("JWT 토큰을 생성한다.")
    @Test
    void JWT_토큰을_생성한다() {
        // given & when
        String actual = jwtTokenProvider.createToken(PAYLOAD);

        // then
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("JWT 토큰의 Payload를 가져온다.")
    @Test
    void JWT_토큰의_Payload를_가져온다() {
        // given
        String token = jwtTokenProvider.createToken(PAYLOAD);

        // when
        String actual = jwtTokenProvider.getPayload(token);

        // then
        assertThat(actual).isEqualTo(PAYLOAD);
    }

    @DisplayName("validateToken 메서드는 만료된 토큰을 전달하면 예외를 던진다.")
    @Test
    void validateToken_메서드는_만료된_토큰을_전달하면_예외를_던진다() {
        // given
        JwtTokenProvider expiredJwtTokenProvider = new JwtTokenProvider(JWT_SECRET_KEY, 0);
        String expiredToken = expiredJwtTokenProvider.createToken(PAYLOAD);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("validateToken 메서드는 유효하지 않은 토큰을 전달하면 예외를 던진다.")
    @Test
    void validateToken_메서드는_유효하지_않은_토큰을_전달하면_예외를_던진다() {
        // given
        String malformedToken = "malformed";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(malformedToken))
                .isInstanceOf(InvalidTokenException.class);
    }
}

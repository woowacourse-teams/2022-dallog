package com.allog.dallog.domain.composition.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_MEMBER;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.common.config.ExternalApiConfig;
import com.allog.dallog.domain.auth.application.AuthBeforeEvent;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(ExternalApiConfig.class)
class AuthGenerateTokenBeforeEventTest {

    @Autowired
    private AuthBeforeEvent authGenerateTokenBeforeEvent;

    @DisplayName("OAuthMember를 전달하면 Member와 OAuth refresh token을 저장한다.")
    @Test
    void OAuthMember를_전달하면_Member와_OAuth_refresh_token을_저장한다() {
        // given
        OAuthMember oAuthMember = STUB_OAUTH_MEMBER();

        // when & then
        assertDoesNotThrow(() -> authGenerateTokenBeforeEvent.process(oAuthMember));
    }

    @DisplayName("잘못된 매개변수를 전달 받는 경우 예외를 던진다.")
    @Test
    void 잘못된_매개변수를_전달_받는_경우_예외를_던진다() {
        // given
        String name = "매트";

        // when & then
        assertThatThrownBy(() -> authGenerateTokenBeforeEvent.process(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

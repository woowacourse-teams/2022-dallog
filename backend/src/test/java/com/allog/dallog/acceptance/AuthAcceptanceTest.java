package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.OAuth_인증_URI를_생성한다;
import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성한다;
import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.토큰이_유효한지_검증한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_401이_반환된다;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.config.TokenConfig;
import com.allog.dallog.domain.auth.dto.OAuthUriResponse;
import com.allog.dallog.domain.auth.dto.TokenResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

@Import(TokenConfig.class)
@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("구글 OAuth 인증 URI를 생성하여 반환한다.")
    @Test
    void 구글_OAuth_인증_URI를_생성하여_반환한다() {
        // given & when
        ExtractableResponse<Response> response = OAuth_인증_URI를_생성한다(GOOGLE_PROVIDER);
        OAuthUriResponse oAuthUriResponse = response.as(OAuthUriResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(oAuthUriResponse.getoAuthUri()).contains("https://");
        });
    }

    @DisplayName("최초 사용자거나 기존에 존재하는 회원인 경우 200을 발급한다.")
    @Test
    void 최초_사용자거나_기존에_존재하는_회원인_경우_200을_발급한다() {
        // given & when
        ExtractableResponse<Response> response = 자체_토큰을_생성한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(tokenResponse.getAccessToken()).isNotEmpty();
        });
    }

    @DisplayName("만료된 토큰으로 웹페이지를 로드하면 상태코드 401을 반환한다.")
    @Test
    void 만료된_토큰으로_웹페이지를_로드하면_상태코드_401을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);

        // when
        ExtractableResponse<Response> response = 토큰이_유효한지_검증한다(accessToken);

        // then
        상태코드_401이_반환된다(response);
    }
}

package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.OAuth_인증_URI를_생성한다;
import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.리프레시_토큰을_통해_새로운_엑세스_토큰을_생성한다;
import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_엑세스_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성한다;
import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.토큰이_유효한지_검증한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_401이_반환된다;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.config.TokenConfig;
import com.allog.dallog.auth.dto.request.TokenRenewalRequest;
import com.allog.dallog.auth.dto.response.AccessAndRefreshTokenResponse;
import com.allog.dallog.auth.dto.response.AccessTokenResponse;
import com.allog.dallog.auth.dto.response.OAuthUriResponse;
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

    @DisplayName("최초 회원이거나 기존에 존재하는 회원이 다시 로그인하는 경우 토큰들을 발급하고 상태코드 200을 반환한다.")
    @Test
    void 최초_회원이거나_기존에_존재하는_회원이_다시_로그인하는_경우_토큰들을_발급하고_상태코드_200을_반환한다() {
        // given & when
        ExtractableResponse<Response> response = 자체_토큰을_생성한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        AccessAndRefreshTokenResponse accessAndRefreshTokenResponse = response.as(AccessAndRefreshTokenResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(accessAndRefreshTokenResponse.getAccessToken()).isNotEmpty();
            assertThat(accessAndRefreshTokenResponse.getRefreshToken()).isNotEmpty();
        });
    }

    @DisplayName("만료된 엑세스_토큰으로 웹페이지를 로드하면 상태코드 401을 반환한다.")
    @Test
    void 만료된_엑세스_토큰으로_웹페이지를_로드하면_상태코드_401을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);

        // when
        ExtractableResponse<Response> response = 토큰이_유효한지_검증한다(accessToken);

        // then
        상태코드_401이_반환된다(response);
    }

    @DisplayName("리프레시 토큰을 통해 새로운 엑세스 토큰을 발급하고 200을 반환한다.")
    @Test
    void 리프레시_토큰을_통해_새로운_엑세스_토큰을_발급하고_200을_반환한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        AccessAndRefreshTokenResponse accessAndRefreshTokenResponse = response.as(AccessAndRefreshTokenResponse.class);
        TokenRenewalRequest tokenRenewalRequest = new TokenRenewalRequest(
                accessAndRefreshTokenResponse.getRefreshToken());

        // when
        ExtractableResponse<Response> actual = 리프레시_토큰을_통해_새로운_엑세스_토큰을_생성한다(tokenRenewalRequest);
        AccessTokenResponse accessTokenResponse = actual.as(AccessTokenResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(actual);
            assertThat(accessTokenResponse.getAccessToken()).isNotEmpty();
        });
    }
}

package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.auth.dto.OAuthUriResponse;
import com.allog.dallog.auth.dto.TokenRequest;
import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.common.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("인증 관련 기능")
@Import(TestConfig.class)
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("구글 OAuth 인증 URI를 생성하여 반환한다.")
    @Test
    void 구글_OAuth_인증_URI를_생성하여_반환한다() {
        // given
        String oAuthProvider = "google";

        // when
        ExtractableResponse<Response> response = OAuth_인증_URI를_생성한다(oAuthProvider);
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
        // given
        String oAuthProvider = "google";
        String code = "sddsfsg4wedfabladsgklvdskfngdakfjgnkd";

        // when
        ExtractableResponse<Response> response = 자체_토큰을_생성한다(oAuthProvider, code);
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(tokenResponse.getAccessToken()).isNotEmpty();
        });
    }

    private ExtractableResponse<Response> OAuth_인증_URI를_생성한다(final String oauthProvider) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/auth/{oauthProvider}/oauth-uri", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> 자체_토큰을_생성한다(final String oauthProvider, final String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(code))
                .when().post("/api/auth/{oauthProvider}/token", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}

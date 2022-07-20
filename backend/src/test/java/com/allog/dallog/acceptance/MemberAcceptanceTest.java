package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.common.fixtures.MemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.MemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.MemberFixtures.PROFILE_IMAGE_URI;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.CODE;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_PROVIDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.common.config.TestConfig;
import com.allog.dallog.member.dto.MemberResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("회원 관련 기능")
@Import(TestConfig.class)
public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("등록된 회원이 자신의 정보를 조회하면 상태코드 200을 반환한다.")
    @Test
    void 등록된_회원이_자신의_정보를_조회하면_상태코드_200_을_반환한다() {
        // given
        TokenResponse tokenResponse = 자체_토큰을_생성한다(OAUTH_PROVIDER, CODE);

        // when
        ExtractableResponse<Response> response = 자신의_정보를_조회한다(tokenResponse);
        MemberResponse memberResponse = response.as(MemberResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
            assertThat(memberResponse.getDisplayName()).isEqualTo(DISPLAY_NAME);
            assertThat(memberResponse.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URI);
        });
    }

    private ExtractableResponse<Response> 자신의_정보를_조회한다(final TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}

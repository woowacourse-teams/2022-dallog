package com.allog.dallog.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.auth.dto.TokenRequest;
import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.config.TestConfig;
import com.allog.dallog.global.dto.CommonResponse;
import com.allog.dallog.member.dto.MemberResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
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

    private static final TypeRef<CommonResponse<TokenResponse>> COMMON_OF_TOKEN_TYPE_REF = new TypeRef<>() {
    };

    private static final TypeRef<CommonResponse<MemberResponse>> COMMON_OF_MEMBER_TYPE_REF = new TypeRef<>() {
    };

    @DisplayName("등록된 회원이 자신의 정보를 조회하면 상태코드 200을 반환한다.")
    @Test
    void 등록된_회원이_자신의_정보를_조회하면_상태코드_200_을_반환한다() {
        // given
        String oauthProvider = "google";
        String code = "sddsfsg4wedfabladsgklvdskfngdakfjgnkd";

        CommonResponse<TokenResponse> commonResponse = 자체_토큰을_생성한다(oauthProvider, code);
        TokenResponse tokenResponse = commonResponse.getData();

        // when
        ExtractableResponse<Response> response = 자신의_정보를_조회한다(tokenResponse);
        CommonResponse<MemberResponse> commonMemberResponse = response.as(COMMON_OF_MEMBER_TYPE_REF);
        MemberResponse memberResponse = commonMemberResponse.getData();

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(memberResponse.getEmail()).isEqualTo("dev.hyeonic@gmail.com");
            assertThat(memberResponse.getDisplayName()).isEqualTo("Fake Name");
            assertThat(memberResponse.getProfileImageUrl()).isEqualTo("Fake Profile Image Url");
        });
    }

    private CommonResponse<TokenResponse> 자체_토큰을_생성한다(final String oauthProvider, final String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(code))
                .when().post("/api/auth/{oauthProvider}/token", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(COMMON_OF_TOKEN_TYPE_REF);
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

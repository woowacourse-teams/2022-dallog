package com.allog.dallog.acceptance.fixtures;

import com.allog.dallog.domain.auth.dto.request.TokenRenewalRequest;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceFixtures {

    public static ExtractableResponse<Response> OAuth_인증_URI를_생성한다(final String oauthProvider) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/auth/{oauthProvider}/oauth-uri?redirectUri={redirectUri}", oauthProvider,
                        "https://dallog.me/oauth")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 자체_토큰을_생성한다(final String oauthProvider, final String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(code, "https://dallog.me/oauth"))
                .when().post("/api/auth/{oauthProvider}/token", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static String 자체_토큰을_생성하고_엑세스_토큰을_반환한다(final String oauthProvider, final String code) {
        TokenResponse tokenResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(code, "https://dallog.me/oauth"))
                .when().post("/api/auth/{oauthProvider}/token", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class);

        return tokenResponse.getAccessToken();
    }

    public static ExtractableResponse<Response> 리프레시_토큰을_통해_새로운_엑세스_토큰을_생성한다(
            final TokenRenewalRequest tokenRenewalRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRenewalRequest)
                .when().get("/api/auth/token/access")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰이_유효한지_검증한다(final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/api/auth/validate/token")
                .then().log().all()
                .extract();
    }
}

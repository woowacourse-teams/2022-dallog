package com.allog.dallog.acceptance.fixtures;

import com.allog.dallog.auth.dto.TokenRequest;
import com.allog.dallog.auth.dto.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceFixtures {

    public static ExtractableResponse<Response> OAuth_인증_URI를_생성한다(final String oauthProvider) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/auth/{oauthProvider}/oauth-uri", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 자체_토큰을_생성한다(final String oauthProvider, final String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(code))
                .when().post("/api/auth/{oauthProvider}/token", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static String 자체_토큰을_생성하고_토큰을_반환한다(final String oauthProvider, final String code) {
        TokenResponse tokenResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(code))
                .when().post("/api/auth/{oauthProvider}/token", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class);

        return tokenResponse.getAccessToken();
    }
}

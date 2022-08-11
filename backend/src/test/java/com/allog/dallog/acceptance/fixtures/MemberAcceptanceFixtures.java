package com.allog.dallog.acceptance.fixtures;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberAcceptanceFixtures {

    public static ExtractableResponse<Response> 자신의_정보를_조회한다(final String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 회원_탈퇴_한다(final String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().delete("/api/members")
                .then().log().all()
                .extract();
    }
}

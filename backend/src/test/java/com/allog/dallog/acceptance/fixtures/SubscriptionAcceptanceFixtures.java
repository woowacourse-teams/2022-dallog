package com.allog.dallog.acceptance.fixtures;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class SubscriptionAcceptanceFixtures {

    public static ExtractableResponse<Response> 구독_목록을_조회한다(final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/members/me/subscriptions")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}

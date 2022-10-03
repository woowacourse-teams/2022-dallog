package com.allog.dallog.acceptance.fixtures;

import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
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

    public static SubscriptionResponse 카테고리를_구독한다(final String accessToken, final Long categoryId) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/members/me/categories/{categoryId}/subscriptions", categoryId)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(SubscriptionResponse.class);
    }
}

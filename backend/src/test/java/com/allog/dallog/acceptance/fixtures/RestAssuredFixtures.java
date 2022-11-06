package com.allog.dallog.acceptance.fixtures;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class RestAssuredFixtures {

    public static ExtractableResponse<Response> get(final String url, final String firstParam,
                                                    final String secondParam) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url, firstParam, secondParam)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> getWithToken(final String url, final String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(final String url, final String param, final Object body) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(url, param)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> postWithToken(final String url, final Object body) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(url)
                .then().log().all()
                .extract();
    }
}

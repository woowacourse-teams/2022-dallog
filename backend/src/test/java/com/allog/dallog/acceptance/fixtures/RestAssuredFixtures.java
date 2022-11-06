package com.allog.dallog.acceptance.fixtures;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class RestAssuredFixtures {

    public static ExtractableResponse<Response> get(final String url, final String... params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getWithToken(final String url, final String accessToken,
                                                             final String... params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get(url, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(final String url, final Object body, final String... params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(url, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postWithToken(final String accessToken, final String url,
                                                              final Object body, final String... params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(url, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> patchWithToken(final String accessToken, final Object body,
                                                               final String url, final String... params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().patch(url, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteWithToken(final String accessToken, final String url,
                                                                final String... params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(url, params)
                .then().log().all()
                .extract();
    }
}

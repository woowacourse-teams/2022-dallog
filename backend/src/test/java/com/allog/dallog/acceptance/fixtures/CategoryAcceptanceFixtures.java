package com.allog.dallog.acceptance.fixtures;

import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class CategoryAcceptanceFixtures {

    public static ExtractableResponse<Response> 새로운_카테고리를_등록한다(final String accessToken, final String name) {
        CategoryCreateRequest request = new CategoryCreateRequest(name);
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/categories")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 카테고리를_페이징을_통해_조회한다(final int page, final int size) {
        return RestAssured.given().log().all()
                .when().get("/api/categories?page={page}&size={size}", page, size)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내가_등록한_카테고리를_페이징을_통해_조회한다(final String accessToken, final int page,
                                                                          final int size) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/api/categories/me?page={page}&size={size}", page, size)
                .then().log().all()
                .extract();
    }
}

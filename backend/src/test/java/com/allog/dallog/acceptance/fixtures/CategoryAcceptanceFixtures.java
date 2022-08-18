package com.allog.dallog.acceptance.fixtures;

import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class CategoryAcceptanceFixtures {

    public static ExtractableResponse<Response> 새로운_카테고리를_등록한다(final String accessToken,
                                                               final CategoryCreateRequest request) {
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

    public static ExtractableResponse<Response> 카테고리를_제목과_페이징을_통해_조회한다(final String name, final int page,
                                                                       final int size) {
        return RestAssured.given().log().all()
                .when().get("/api/categories?name={name}&page={page}&size={size}", name, page, size)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내가_등록한_카테고리를_제목과_페이징을_통해_조회한다(
            final String accessToken, final String name, final int page, final int size) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/api/categories/me?name={name}&page={page}&size={size}", name, page, size)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> id를_통해_카테고리를_가져온다(final Long id) {
        return RestAssured.given().log().all()
                .when().get("/api/categories/{categoryId}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내가_등록한_카테고리를_수정한다(final String accessToken, final Long id,
                                                                  final String name) {
        CategoryUpdateRequest request = new CategoryUpdateRequest(name);
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().patch("/api/categories/{categoryId}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내가_등록한_카테고리를_삭제한다(final String accessToken, final Long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/api/categories/{categoryId}", id)
                .then().log().all()
                .extract();
    }
}

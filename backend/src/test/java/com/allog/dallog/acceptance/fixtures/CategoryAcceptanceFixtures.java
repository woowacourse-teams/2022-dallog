package com.allog.dallog.acceptance.fixtures;

import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.categoryrole.dto.request.CategoryRoleUpdateRequest;
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

    public static ExtractableResponse<Response> 전체_카테고리를_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/api/categories")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 전체_카테고리를_제목_검색을_통해_조회한다(final String name) {
        return RestAssured.given().log().all()
                .when().get("/api/categories?name={name}", name)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> id를_통해_카테고리를_조회한다(final Long id) {
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

    public static ExtractableResponse<Response> 회원의_카테고리_역할을_변경한다(final String accessToken, final Long categoryId,
                                                                  final Long memberId,
                                                                  final CategoryRoleUpdateRequest request) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().patch("/api/categories/{categoryId}/subscribers/{memberId}/role", categoryId, memberId)
                .then().log().all()
                .extract();
    }
}

package com.allog.dallog.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.global.dto.FindSliceResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("카테고리 관련 기능")
public class CategoryAcceptanceTest extends AcceptanceTest {

    @DisplayName("정상적인 카테고리 정보를 등록하면 상태코드 201을 반환한다.")
    @Test
    void 정상적인_카테고리_정보를_등록하면_상태코드_201을_반환한다() {
        // given
        String name = "BE 공식일정";

        // when
        ExtractableResponse<Response> response = 새로운_카테고리를_등록한다(name);

        // then
        상태코드_201이_반환된다(response);
    }

    @DisplayName("카테고리를 등록하고 페이징을 통해 나누어 조회한다.")
    @Test
    void 카테고리를_등록하고_페이징을_통해_나누어_조회한다() {
        // given
        새로운_카테고리를_등록한다("BE 공식일정");
        새로운_카테고리를_등록한다("FE 공식일정");
        새로운_카테고리를_등록한다("알록달록 회의");

        int page = 1;
        int size = 8;

        // when
        ExtractableResponse<Response> response = 카테고리를_페이징을_통해_조회한다(page, size);
        FindSliceResponse findSliceResponse = response.as(FindSliceResponse.class);

        // then
        assertAll(
                () -> 상태코드_200이_반환된다(response),
                () -> assertThat(findSliceResponse.getPage()).isEqualTo(page),
                () -> assertThat(findSliceResponse.getTotalCount()).isEqualTo(3),
                () -> assertThat(findSliceResponse.getData()).hasSize(3)
        );
    }

    private ExtractableResponse<Response> 새로운_카테고리를_등록한다(final String name) {
        CategoryCreateRequest request = new CategoryCreateRequest(name);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/categories")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 카테고리를_페이징을_통해_조회한다(final int page, final int size) {
        return RestAssured.given().log().all()
                .when().get("/api/categories?page={page}&size={size}", page, size)
                .then().log().all()
                .extract();
    }
}

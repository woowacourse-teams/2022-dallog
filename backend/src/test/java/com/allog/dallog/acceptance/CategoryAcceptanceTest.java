package com.allog.dallog.acceptance;

import com.allog.dallog.category.dto.request.CategoryCreateRequest;
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

    private ExtractableResponse<Response> 새로운_카테고리를_등록한다(final String name) {
        CategoryCreateRequest request = new CategoryCreateRequest(name);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/categories")
                .then().log().all()
                .extract();
    }
}

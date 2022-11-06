package com.allog.dallog.acceptance;

import static com.allog.dallog.common.Constants.취업_일정_메모;
import static com.allog.dallog.common.Constants.취업_일정_시작일;
import static com.allog.dallog.common.Constants.취업_일정_제목;
import static com.allog.dallog.common.Constants.취업_일정_종료일;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.몇시간_네번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.몇시간_두번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.몇시간_세번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.몇시간_첫번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_네번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_다섯번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_두번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_세번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.장기간_첫번째_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.종일_두번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.종일_세번째_일정;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.종일_첫번째_일정;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;

import com.allog.dallog.acceptance.builder.AuthAssuredBuilder;
import com.allog.dallog.acceptance.builder.AuthAssuredBuilder.AuthResponseBuilder;
import com.allog.dallog.acceptance.builder.CategoryAssuredBuilder;
import com.allog.dallog.acceptance.builder.CategoryAssuredBuilder.CategoryReqeustBuilder;
import com.allog.dallog.acceptance.builder.ScheduleAssuredBuilder;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("일정 관련 기능")
class ScheduleAcceptanceTest extends AcceptanceTest {

    private final CategoryCreateRequest 취업_카테고리_생성_요청 = new CategoryCreateRequest(취업_카테고리_이름, NORMAL);
    private final ScheduleCreateRequest 취업_일정_생성_요청 = new ScheduleCreateRequest(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일,
            취업_일정_메모);

    @DisplayName("일정을 등록하면 상태코드 201을 받는다.")
    @Test
    void 정상적인_일정정보를_등록하면_상태코드_201을_반환한다() {
        AuthResponseBuilder 관리자 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, "creator authorization code")
                .응답()
                .토큰들을_발급_받는다();

        CategoryReqeustBuilder 대학입시_카테고리 = CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(관리자.accessToken(), 취업_카테고리_생성_요청);

        ScheduleAssuredBuilder.요청()
                .일정을_등록한다(관리자.accessToken(), 취업_일정_생성_요청, 대학입시_카테고리.getId())
                .응답()
                .상태코드_201을_받는다();
    }

    @DisplayName("카테고리로 일정을 조회하면 상태코드 200을 받는다.")
    @Test
    void 카테고리로_일정을_조회하면_상태코드_200을_반환한다() {
        AuthResponseBuilder 관리자 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, "creator authorization code")
                .응답()
                .토큰들을_발급_받는다();

        CategoryReqeustBuilder 대학입시_카테고리 = CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(관리자.accessToken(), 취업_카테고리_생성_요청);

        ScheduleAssuredBuilder.요청()
                .일정을_등록한다(관리자.accessToken(), 장기간_첫번째_요청, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 장기간_두번째_요청, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 장기간_세번째_요청, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 장기간_네번째_요청, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 장기간_다섯번째_요청, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 종일_첫번째_일정, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 종일_두번째_일정, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 종일_세번째_일정, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 몇시간_첫번째_일정, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 몇시간_두번째_일정, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 몇시간_세번째_일정, 대학입시_카테고리.getId())
                .일정을_등록한다(관리자.accessToken(), 몇시간_네번째_일정, 대학입시_카테고리.getId())
                .월별_일정을_조회한다(관리자.accessToken(), 대학입시_카테고리.getId(), "2022-07-01T00:00", "2022-08-15T23:59")
                .응답()
                .상태코드_200을_받는다();
    }

    @DisplayName("일정을 단건조회_하면 상태코드 200을 받는다.")
    @Test
    void 일정을_단건조회_하면_상태코드_200을_받는다() {
        AuthResponseBuilder 관리자 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, "creator authorization code")
                .응답()
                .토큰들을_발급_받는다();

        CategoryReqeustBuilder 대학입시_카테고리 = CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(관리자.accessToken(), 취업_카테고리_생성_요청);

        ScheduleAssuredBuilder.요청()
                .일정을_등록한다(관리자.accessToken(), 취업_일정_생성_요청, 대학입시_카테고리.getId())
                .단건_일정을_조회한다(관리자.accessToken())
                .응답()
                .상태코드_200을_받는다();
    }

    @DisplayName("일정을 수정하면 상태코드 204를 받는다.")
    @Test
    void 일정을_수정하면_상태코드_204를_받는다() {
        AuthResponseBuilder 관리자 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, "creator authorization code")
                .응답()
                .토큰들을_발급_받는다();

        CategoryReqeustBuilder 대학입시_카테고리 = CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(관리자.accessToken(), 취업_카테고리_생성_요청);

        ScheduleAssuredBuilder.요청()
                .일정을_등록한다(관리자.accessToken(), 취업_일정_생성_요청, 대학입시_카테고리.getId())
                .일정을_수정한다(관리자.accessToken(), 일정_수정_요청(대학입시_카테고리.getId()))
                .응답()
                .상태코드_204을_받는다();
    }

    @DisplayName("일정을 삭제하면 상태코드 204를 받는다.")
    @Test
    void 일정을_삭제하면_상태코드_204를_받는다() {
        AuthResponseBuilder 관리자 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, "creator authorization code")
                .응답()
                .토큰들을_발급_받는다();

        CategoryReqeustBuilder 대학입시_카테고리 = CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(관리자.accessToken(), 취업_카테고리_생성_요청);

        ScheduleAssuredBuilder.요청()
                .일정을_등록한다(관리자.accessToken(), 취업_일정_생성_요청, 대학입시_카테고리.getId())
                .일정을_삭제한다(관리자.accessToken())
                .응답()
                .상태코드_204을_받는다();
    }

    private ScheduleUpdateRequest 일정_수정_요청(Long categoryId) {
        return new ScheduleUpdateRequest(categoryId, 취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);
    }
}

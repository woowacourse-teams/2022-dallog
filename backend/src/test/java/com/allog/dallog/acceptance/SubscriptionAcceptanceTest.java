package com.allog.dallog.acceptance;

import static com.allog.dallog.common.Constants.대학입시_카테고리_이름;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.acceptance.builder.AuthAssuredBuilder;
import com.allog.dallog.acceptance.builder.AuthAssuredBuilder.AuthResponseBuilder;
import com.allog.dallog.acceptance.builder.CategoryAssuredBuilder;
import com.allog.dallog.acceptance.builder.CategoryAssuredBuilder.CategoryReqeustBuilder;
import com.allog.dallog.acceptance.builder.SubscritptionAssuredBuilder;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구독 관련 기능")
public class SubscriptionAcceptanceTest extends AcceptanceTest {

    private final CategoryCreateRequest 대학입시_카테고리_생성_요청 = new CategoryCreateRequest(대학입시_카테고리_이름, NORMAL);
    private final SubscriptionUpdateRequest 구독_카테고리_색상_변경_요청 = new SubscriptionUpdateRequest(COLOR_1, true);

    @DisplayName("카테고리를 구독하면 상태코드 200을 받는다.")
    @Test
    void 카테고리를_구독하면_상태코드_200을_받는다() {
        AuthResponseBuilder 관리자 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, "creator authorization code")
                .응답()
                .토큰들을_발급_받는다();

        CategoryReqeustBuilder 대학입시_카테고리 = CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(관리자.accessToken(), 대학입시_카테고리_생성_요청);

        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .상태코드_200을_받는다()
                .토큰들을_발급_받는다();

        SubscritptionAssuredBuilder.요청()
                .카테고리를_구독한다(회원.accessToken(), 대학입시_카테고리.getId())
                .응답()
                .상태코드_201을_받는다();
    }

    @DisplayName("자신의 구독 정보를 수정하면 상태코드 204를 받는다.")
    @Test
    void 자신의_구독_정보를_수정하면_상태코드_204를_받는다() {
        AuthResponseBuilder 관리자 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, "creator authorization code")
                .응답()
                .토큰들을_발급_받는다();

        CategoryReqeustBuilder 대학입시_카테고리 = CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(관리자.accessToken(), 대학입시_카테고리_생성_요청);

        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .상태코드_200을_받는다()
                .토큰들을_발급_받는다();

        SubscritptionAssuredBuilder.요청()
                .카테고리를_구독한다(회원.accessToken(), 대학입시_카테고리.getId())
                .구독_정보를_변경한다(회원.accessToken(), 구독_카테고리_색상_변경_요청)
                .응답()
                .상태코드_204을_받는다();
    }

    @DisplayName("구독을 취소하면 상태코드 204를 받는다.")
    @Test
    void 구독을_취소하면_상태코드_204를_받는다() {
        AuthResponseBuilder 관리자 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, "creator authorization code")
                .응답()
                .토큰들을_발급_받는다();

        CategoryReqeustBuilder 대학입시_카테고리 = CategoryAssuredBuilder.요청()
                .카테고리를_등록한다(관리자.accessToken(), 대학입시_카테고리_생성_요청);

        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .상태코드_200을_받는다()
                .토큰들을_발급_받는다();

        SubscritptionAssuredBuilder.요청()
                .카테고리를_구독한다(회원.accessToken(), 대학입시_카테고리.getId())
                .구독을_취소한다(회원.accessToken())
                .응답()
                .상태코드_204을_받는다();
    }
}

package com.allog.dallog.presentation;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_응답;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_응답;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_응답;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자_응답;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트_응답;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상1_구독_응답;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상2_구독_응답;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상3_구독_응답;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class SubscriptionControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaa.bbbbb.ccccc";

    @DisplayName("회원과 카테고리 정보를 기반으로 구독한다.")
    @Test
    void 회원과_카테고리_정보를_기반으로_구독한다() throws Exception {
        // given
        CategoryResponse 공통_일정_응답 = 공통_일정_응답(관리자_응답);
        SubscriptionResponse 색상1_구독_응답 = 색상1_구독_응답(공통_일정_응답);

        given(authService.extractMemberId(any())).willReturn(매트_응답.getId());
        given(subscriptionService.save(any(), any())).willReturn(색상1_구독_응답);

        // when & then
        mockMvc.perform(post("/api/members/me/categories/{categoryId}/subscriptions", 공통_일정_응답.getId())
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("subscription/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )))
                .andExpect(status().isCreated());
    }

    @DisplayName("회원이 이미 카테고리를 구독한 경우 예외를 던진다.")
    @Test
    void 회원이_이미_카테고리를_구독한_경우_예외를_던진다() throws Exception {
        // given
        given(authService.extractMemberId(any())).willReturn(매트_응답.getId());
        given(subscriptionService.save(any(), any())).willThrow(new ExistSubscriptionException());

        // when & then
        mockMvc.perform(
                        post("/api/members/me/categories/{categoryId}/subscriptions", 1L)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("subscription/save/failByAlreadyExisting",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("타인의 개인 카테고리 구독 요청시 403 Forbidden을 반환한다.")
    @Test
    void 타인의_개인_카테고리_구독_요청시_403_Forbidden을_반환한다() throws Exception {
        // given
        given(subscriptionService.save(any(), any()))
                .willThrow(new NoPermissionException("구독 권한이 없는 카테고리입니다."));

        // when & then
        mockMvc.perform(
                        post("/api/members/me/categories/{categoryId}/subscriptions", 1L)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("subscription/save/failBySubscribingPrivateCategoryOfOther",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )))
                .andExpect(status().isForbidden());
    }

    @DisplayName("자신의 구독 정보를 조회한다.")
    @Test
    void 자신의_구독_정보를_조회한다() throws Exception {
        // given
        CategoryResponse 공통_일정_응답 = 공통_일정_응답(관리자_응답);
        CategoryResponse BE_일정_응답 = BE_일정_응답(관리자_응답);
        CategoryResponse FE_일정_응답 = FE_일정_응답(관리자_응답);

        SubscriptionResponse 색상1_구독_응답 = 색상1_구독_응답(공통_일정_응답);
        SubscriptionResponse 색상2_구독_응답 = 색상2_구독_응답(BE_일정_응답);
        SubscriptionResponse 색상3_구독_응답 = 색상3_구독_응답(FE_일정_응답);

        given(authService.extractMemberId(any())).willReturn(매트_응답.getId());

        List<SubscriptionResponse> subscriptionResponses = List.of(색상1_구독_응답, 색상2_구독_응답, 색상3_구독_응답);
        given(subscriptionService.findByMemberId(any())).willReturn(new SubscriptionsResponse(subscriptionResponses));

        // when & then
        mockMvc.perform(get("/api/members/me/subscriptions")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("subscription/findMine",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )))
                .andExpect(status().isOk());
    }

    @DisplayName("자신의 구독 정보를 수정한다.")
    @Test
    void 자신의_구독_정보를_수정한다() throws Exception {
        // given
        CategoryResponse 공통_일정_응답 = 공통_일정_응답(관리자_응답);
        SubscriptionResponse 색상1_구독_응답 = 색상1_구독_응답(공통_일정_응답);
        SubscriptionUpdateRequest request = new SubscriptionUpdateRequest(Color.COLOR_2, true);

        given(authService.extractMemberId(any())).willReturn(매트_응답.getId());
        willDoNothing().given(subscriptionService)
                .update(색상1_구독_응답.getId(), 매트_응답.getId(), request);

        // when & then
        mockMvc.perform(patch("/api/members/me/subscriptions/{subscriptionId}", 색상1_구독_응답.getId())
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("subscription/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("subscriptionId").description("구독 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("colorCode").type(JsonFieldType.STRING).description("구독 색 정보"),
                                fieldWithPath("checked").type(JsonFieldType.BOOLEAN).description("체크 유무")
                        )))
                .andExpect(status().isNoContent());
    }

    @DisplayName("구독 id를 기반으로 자신의 구독 정보를 삭제한다.")
    @Test
    void 구독_id를_기반으로_자신의_구독_정보를_삭제한다() throws Exception {
        // given
        CategoryResponse 공통_일정_응답 = 공통_일정_응답(관리자_응답);
        SubscriptionResponse 색상1_구독_응답 = 색상1_구독_응답(공통_일정_응답);

        given(authService.extractMemberId(any())).willReturn(매트_응답.getId());
        willDoNothing().given(subscriptionService)
                .delete(색상1_구독_응답.getId(), 매트_응답.getId());

        // when & then
        mockMvc.perform(delete("/api/members/me/subscriptions/{subscriptionId}", 색상1_구독_응답.getId())
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("subscription/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("subscriptionId").description("구독 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )))
                .andExpect(status().isNoContent());
    }

    @DisplayName("구독 제거시 자신이 가지고 있지 않은 구독 정보인 경우 예외를 던진다.")
    @Test
    void 구독_제거시_자신이_가지고_있지_않은_구독_정보인_경우_예외를_던진다() throws Exception {
        // given
        given(authService.extractMemberId(any())).willReturn(매트_응답.getId());
        willThrow(new NoPermissionException())
                .willDoNothing()
                .given(subscriptionService)
                .delete(any(), any());

        // when & then
        mockMvc.perform(delete("/api/members/me/subscriptions/{subscriptionId}", 1L)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("subscription/delete/failByNoPermission",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("subscriptionId").description("구독 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )))
                .andExpect(status().isForbidden());
    }
}

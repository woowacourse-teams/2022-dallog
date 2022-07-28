package com.allog.dallog.domain.subscription.presentation;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_응답;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_응답;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_응답;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자_응답;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트_응답;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.노란색_구독_응답;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.빨간색_구독_생성_요청;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.빨간색_구독_응답;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.파란색_구독_응답;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaa.bbbbb.ccccc";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private SubscriptionService subscriptionService;

    @DisplayName("회원과 카테고리 정보를 기반으로 구독한다.")
    @Test
    void 회원과_카테고리_정보를_기반으로_구독한다() throws Exception {
        // given
        CategoryResponse 공통_일정_응답 = 공통_일정_응답(관리자_응답);
        SubscriptionResponse 빨간색_구독_응답 = 빨간색_구독_응답(공통_일정_응답);

        given(authService.extractMemberId(any())).willReturn(매트_응답.getId());
        given(subscriptionService.save(any(), any(), any())).willReturn(빨간색_구독_응답);

        // when & then
        mockMvc.perform(post("/api/members/me/categories/{categoryId}/subscriptions", 1L)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(빨간색_구독_생성_요청)))
                .andDo(print())
                .andDo(document("subscription/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("color").type(JsonFieldType.STRING).description("구독 색 장보")
                        )))
                .andExpect(status().isCreated());
    }

    @DisplayName("자신의 구독 정보를 조회한다.")
    @Test
    void 자신의_구독_정보를_조회한다() throws Exception {
        // given
        CategoryResponse 공통_일정_응답 = 공통_일정_응답(관리자_응답);
        CategoryResponse BE_일정_응답 = BE_일정_응답(관리자_응답);
        CategoryResponse FE_일정_응답 = FE_일정_응답(관리자_응답);

        SubscriptionResponse 빨간색_구독_응답 = 빨간색_구독_응답(공통_일정_응답);
        SubscriptionResponse 파란색_구독_응답 = 파란색_구독_응답(BE_일정_응답);
        SubscriptionResponse 노란색_구독_응답 = 노란색_구독_응답(FE_일정_응답);

        given(authService.extractMemberId(any())).willReturn(매트_응답.getId());

        List<SubscriptionResponse> subscriptionResponses = List.of(빨간색_구독_응답, 파란색_구독_응답, 노란색_구독_응답);
        given(subscriptionService.findByMemberId(any())).willReturn(new SubscriptionsResponse(subscriptionResponses));

        // when & then
        mockMvc.perform(get("/api/members/me/subscriptions")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("subscription/me",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )))
                .andExpect(status().isOk());
    }

    @DisplayName("구독 id를 기반으로 자신의 구독 정보를 삭제한다.")
    @Test
    void 구독_id를_기반으로_자신의_구독_정보를_삭제한다() throws Exception {
        // given
        CategoryResponse 공통_일정_응답 = 공통_일정_응답(관리자_응답);
        SubscriptionResponse 빨간색_구독_응답 = 빨간색_구독_응답(공통_일정_응답);

        given(authService.extractMemberId(any())).willReturn(매트_응답.getId());
        willDoNothing().given(subscriptionService).deleteByIdAndMemberId(빨간색_구독_응답.getId(), 매트_응답.getId());

        // when & then
        mockMvc.perform(delete("/api/members/me/subscriptions/{subscriptionId}", 빨간색_구독_응답.getId())
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
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
}

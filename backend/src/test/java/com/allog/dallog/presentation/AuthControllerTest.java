package com.allog.dallog.presentation;

import static com.allog.dallog.common.fixtures.AuthFixtures.OAUTH_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.OAuth_로그인_링크;
import static com.allog.dallog.common.fixtures.AuthFixtures.인증_코드;
import static com.allog.dallog.common.fixtures.AuthFixtures.인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.인증_코드_토큰_응답;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.infrastructure.oauth.exception.OAuthException;
import com.allog.dallog.presentation.auth.AuthController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @DisplayName("OAuth 소셜 로그인을 위한 링크와 상태코드 200을 반환한다.")
    @Test
    void OAuth_소셜_로그인을_위한_링크와_상태코드_200을_반환한다() throws Exception {
        // given
        given(authService.generateGoogleLink()).willReturn(OAuth_로그인_링크);

        // when & then
        mockMvc.perform(get("/api/auth/{oauthProvider}/oauth-uri", OAUTH_PROVIDER))
                .andDo(print())
                .andDo(document("auth/link",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("oauthProvider").description("OAuth 로그인 제공자")
                        ),
                        responseFields(
                                fieldWithPath("oAuthUri").type(JsonFieldType.STRING).description("OAuth 소셜 로그인 링크")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("OAuth 로그인을 하면 token과 상태코드 200을 반환한다.")
    @Test
    void OAuth_로그인을_하면_token과_상태코드_200을_반환한다() throws Exception {
        // given
        given(authService.generateTokenWithCode(인증_코드)).willReturn(인증_코드_토큰_응답());

        // when & then
        mockMvc.perform(post("/api/auth/{oauthProvider}/token", OAUTH_PROVIDER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(인증_코드_토큰_요청())))
                .andDo(print())
                .andDo(document("auth/token",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("oauthProvider").description("OAuth 로그인 제공자")
                        ),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("OAuth 로그인 인증 코드")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("OAuth 로그인 과정에서 Resource Server 에러가 발생하면 상태코드 500을 반환한다.")
    @Test
    void OAuth_로그인_과정에서_Resource_Server_에러가_발생하면_상태코드_500을_반환한다() throws Exception {
        // given
        given(authService.generateTokenWithCode(인증_코드)).willThrow(new OAuthException());

        // when & then
        mockMvc.perform(post("/api/auth/{oauthProvider}/token", OAUTH_PROVIDER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(인증_코드_토큰_요청())))
                .andDo(print())
                .andDo(document("auth/exception/token",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("oauthProvider").description("OAuth 로그인 제공자")
                        ),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("OAuth 로그인 인증 코드")
                        )
                ))
                .andExpect(status().isInternalServerError());
    }
}

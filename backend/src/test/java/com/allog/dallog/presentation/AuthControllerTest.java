package com.allog.dallog.presentation;

import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.MEMBER_리뉴얼_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.MEMBER_리뉴얼_토큰_응답;
import static com.allog.dallog.common.fixtures.AuthFixtures.MEMBER_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.MEMBER_인증_코드_토큰_응답;
import static com.allog.dallog.common.fixtures.AuthFixtures.OAUTH_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.OAuth_로그인_링크;
import static org.mockito.ArgumentMatchers.any;
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
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.application.OAuthUri;
import com.allog.dallog.domain.auth.exception.InvalidTokenException;
import com.allog.dallog.infrastructure.oauth.exception.OAuthException;
import com.allog.dallog.presentation.auth.AuthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private OAuthUri oAuthUri;

    @DisplayName("OAuth 소셜 로그인을 위한 링크와 상태코드 200을 반환한다.")
    @Test
    void OAuth_소셜_로그인을_위한_링크와_상태코드_200을_반환한다() throws Exception {
        // given
        given(oAuthUri.generate(any())).willReturn(OAuth_로그인_링크);

        // when & then
        mockMvc.perform(get("/api/auth/{oauthProvider}/oauth-uri?redirectUri={redirectUri}", GOOGLE_PROVIDER,
                        "https://dallog.me/oauth"))
                .andDo(print())
                .andDo(document("auth/generateLink",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("oauthProvider").description("OAuth 로그인 제공자 (GOOGLE)")
                        ),
                        requestParameters(
                                parameterWithName("redirectUri").description("OAuth Redirect URI")
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
        given(authService.generateAccessAndRefreshToken(any())).willReturn(MEMBER_인증_코드_토큰_응답());

        // when & then
        mockMvc.perform(post("/api/auth/{oauthProvider}/token", OAUTH_PROVIDER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MEMBER_인증_코드_토큰_요청())))
                .andDo(print())
                .andDo(document("auth/generateTokens",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("oauthProvider").description("OAuth 로그인 제공자")
                        ),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("OAuth 로그인 인증 코드"),
                                fieldWithPath("redirectUri").type(JsonFieldType.STRING)
                                        .description("OAuth Redirect URI")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("달록 Access Token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("달록 Refresh Token")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("OAuth 로그인 과정에서 Resource Server 에러가 발생하면 상태코드 500을 반환한다.")
    @Test
    void OAuth_로그인_과정에서_Resource_Server_에러가_발생하면_상태코드_500을_반환한다() throws Exception {
        // given
        given(authService.generateAccessAndRefreshToken(any())).willThrow(new OAuthException());

        // when & then
        mockMvc.perform(post("/api/auth/{oauthProvider}/token", OAUTH_PROVIDER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MEMBER_인증_코드_토큰_요청())))
                .andDo(print())
                .andDo(document("auth/generateTokens/failByResourceServerError",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("oauthProvider").description("OAuth 로그인 제공자")
                        ),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("OAuth 로그인 인증 코드"),
                                fieldWithPath("redirectUri").type(JsonFieldType.STRING)
                                        .description("OAuth Redirect URI")
                        )
                ))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("리프레시 토큰을 통해 새로운 엑세스 토큰을 발급하면 상태코드 200을 반환한다.")
    @Test
    void 리프레시_토큰을_통해_새로운_엑세스_토큰을_발급하면_상태코드_200을_반환한다() throws Exception {
        // given
        given(authService.generateAccessToken(any())).willReturn(MEMBER_리뉴얼_토큰_응답());

        // when & then
        mockMvc.perform(post("/api/auth/token/access")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MEMBER_리뉴얼_토큰_요청())))
                .andDo(print())
                .andDo(document("auth/generateRenewalToken",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("OAuth 리프레시 토큰 인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("달록 Renewal Access Token")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("잘못된 리프레시 토큰으로 새로운 엑세스 토큰을 발급하려 하면 상태코드 401을 반환한다.")
    @Test
    void 존재하지_않는_리프레시_토큰으로_새로운_엑세스_토큰을_발급하려_하면_상태코드_401을_반환한다() throws Exception {
        // given
        given(authService.generateAccessToken(any())).willThrow(new InvalidTokenException());

        // when & then
        mockMvc.perform(post("/api/auth/token/access")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MEMBER_리뉴얼_토큰_요청())))
                .andDo(print())
                .andDo(document("auth/generateRenewalToken/invalidTokenError",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("OAuth 리프레시 토큰 인증 코드")
                        )
                ))
                .andExpect(status().isUnauthorized());
    }
}

package com.allog.dallog.domain.auth.presentation;

import static com.allog.dallog.common.fixtures.AuthFixtures.OAUTH_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.OAuth_로그인_링크;
import static com.allog.dallog.common.fixtures.AuthFixtures.인증_코드;
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
import com.allog.dallog.domain.auth.dto.TokenRequest;
import com.allog.dallog.infrastructure.oauth.exception.OAuthException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(AuthController.class)
class AuthControllerTest {

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

        // when & than
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

    //TODO : OAuth로그인 시 토큰 발급 테스트 만들어야함.

    @DisplayName("OAuth 로그인을 할 떄 Resource Server 에러시 상태코드 500을 반환한다.")
    @Test
    void OAuth_로그인을_할_때_Resource_Server_에러시_상태코드_500을_반환한다() throws Exception {
        // given
        given(authService.generateTokenWithCode(인증_코드)).willThrow(new OAuthException());

        TokenRequest request = new TokenRequest(인증_코드);

        // when & than
        mockMvc.perform(post("/api/auth/{oauthProvider}/token", OAUTH_PROVIDER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
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

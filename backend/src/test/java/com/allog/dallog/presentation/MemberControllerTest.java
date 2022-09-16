package com.allog.dallog.presentation;

import static com.allog.dallog.common.fixtures.AuthFixtures.더미_엑세스_토큰;
import static com.allog.dallog.common.fixtures.AuthFixtures.토큰_정보;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_응답;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberUpdateRequest;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";

    @MockBean
    private AuthService authService;

    @MockBean
    private MemberService memberService;

    @DisplayName("자신의 회원 정보를 조회한다.")
    @Test
    void 자신의_회원_정보를_조회한다() throws Exception {
        //given
        given(memberService.findById(파랑_응답.getId())).willReturn(파랑_응답);
        given(authService.extractMemberId(더미_엑세스_토큰)).willReturn(파랑_응답.getId());

        // when & then
        mockMvc.perform(get("/api/members/me")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("member/findMe",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 ID"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("displayName").description("회원 이름"),
                                fieldWithPath("profileImageUrl").description("회원 프로필 이미지 URL"),
                                fieldWithPath("socialType").description("회원 소셜 타입")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("존재하지 않는 회원의 정보를 조회하려고 하면 예외를 발생한다.")
    @Test
    void 존재하지_않는_회원의_정보를_조회하려고_하면_예외를_발생한다() throws Exception {
        // given
        given(memberService.findById(0L)).willThrow(new NoSuchMemberException());

        // when & then
        mockMvc.perform(get("/api/members/me")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("member/findMe/failNoMember",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("등록된 회원이 자신의 이름을 수정한다.")
    @Test
    void 등록된_회원이_자신의_이름을_수정한다() throws Exception {
        // given
        willDoNothing()
                .given(memberService)
                .update(any(), any());
        MemberUpdateRequest 회원_수정_요청 = new MemberUpdateRequest("패트");

        // when & then
        mockMvc.perform(patch("/api/members/me")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .content(objectMapper.writeValueAsString(회원_수정_요청))
                )
                .andDo(print())
                .andDo(document("member/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("displayName").type(JsonFieldType.STRING).description("수정할 이름")
                        )))
                .andExpect(status().isNoContent());
    }

    @DisplayName("등록된 회원이 회원탈퇴 한다.")
    @Test
    void 등록된_회원이_회원탈퇴_한다() throws Exception {
        // given
        willDoNothing()
                .given(memberService)
                .deleteById(any());

        // when & then
        mockMvc.perform(delete("/api/members/me")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                )
                .andDo(print())
                .andDo(document("member/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        )))
                .andExpect(status().isNoContent());
    }
}

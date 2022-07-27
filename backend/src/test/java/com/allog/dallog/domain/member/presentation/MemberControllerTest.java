package com.allog.dallog.domain.member.presentation;

import static com.allog.dallog.common.fixtures.AuthFixtures.더미_엑세스_토큰;
import static com.allog.dallog.common.fixtures.AuthFixtures.토큰_정보;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이메일;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_프로필;
import static com.allog.dallog.domain.member.domain.SocialType.GOOGLE;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper ObjectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private MemberService memberService;

    @DisplayName("자신의 회원 정보를 조회한다.")
    @Test
    void 자신의_회원_정보를_조회한다() throws Exception {
        //given
        MemberResponse response = new MemberResponse(1L, 파랑_이메일, 파랑_이름, 파랑_프로필, GOOGLE);

        given(memberService.findById(response.getId())).willReturn(response);
        given(authService.extractMemberId(더미_엑세스_토큰)).willReturn(response.getId());

        // when & then
        mockMvc.perform(get("/api/members/me")
                        .header("Authorization", 토큰_정보)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("member/me",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰"))
                ))
                .andExpect(status().isOk());
    }
}

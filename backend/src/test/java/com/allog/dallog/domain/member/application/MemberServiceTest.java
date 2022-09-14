package com.allog.dallog.domain.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.fixtures.AuthFixtures;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.member.dto.MemberUpdateRequest;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends ServiceTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("id를 통해 회원을 단건 조회한다.")
    @Test
    void id를_통해_회원을_단건_조회한다() {
        // given
        Long 파랑_id = parseMemberId(AuthFixtures.파랑_인증_코드_토큰_요청());

        // when & then
        assertThat(memberService.findById(파랑_id).getId())
                .isEqualTo(파랑_id);
    }

    @DisplayName("회원의 이름을 수정한다.")
    @Test
    void 회원의_이름을_수정한다() {
        // given
        Long 매트_id = parseMemberId(AuthFixtures.매트_인증_코드_토큰_요청());

        String 패트_이름 = "패트";
        MemberUpdateRequest 매트_수정_요청 = new MemberUpdateRequest(패트_이름);

        // when
        memberService.update(매트_id, 매트_수정_요청);

        // then
        MemberResponse actual = memberService.findById(매트_id);
        assertThat(actual.getDisplayName()).isEqualTo(패트_이름);
    }

    @DisplayName("회원을 제거한다.")
    @Test
    void 회원을_제거한다() {
        // given
        Long 후디_id = parseMemberId(AuthFixtures.후디_인증_코드_토큰_요청());

        // when
        memberService.deleteById(후디_id);

        // then
        assertThatThrownBy(() -> memberService.findById(후디_id))
                .isInstanceOf(NoSuchMemberException.class);
    }
}

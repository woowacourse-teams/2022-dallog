package com.allog.dallog.member.application;

import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.builder.GivenBuilder;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.member.dto.request.MemberUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends ServiceTest {

    private final MemberUpdateRequest 나인_이름_수정_요청 = new MemberUpdateRequest("텐");

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원을 조회한다.")
    @Test
    void 회원을_조회한다() {
        // given
        GivenBuilder 나인 = 나인();

        // when & then
        assertThat(memberService.findById(나인.회원().getId()).getId())
                .isEqualTo(나인.회원().getId());
    }

    @DisplayName("회원의 이름을 수정한다.")
    @Test
    void 회원의_이름을_수정한다() {
        // given
        GivenBuilder 나인 = 나인();

        // when
        memberService.update(나인.회원().getId(), 나인_이름_수정_요청);

        // then
        Member actual = memberRepository.getById(나인.회원().getId());
        assertThat(actual.getDisplayName()).isEqualTo("텐");
    }
}

package com.allog.dallog.domain.member.application;

import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.member.dto.MemberUpdateRequest;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends ServiceTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("회원을 저장한다.")
    @Test
    void 회원을_저장한다() {
        // given & when
        MemberResponse 파랑 = memberService.save(파랑());

        // then
        assertThat(파랑).isNotNull();
    }

    @DisplayName("id를 통해 회원을 단건 조회한다.")
    @Test
    void id를_통해_회원을_단건_조회한다() {
        // given
        MemberResponse 파랑 = memberService.save(파랑());

        // when & then
        assertThat(memberService.findById(파랑.getId()))
                .usingRecursiveComparison()
                .isEqualTo(파랑);
    }

    @DisplayName("회원의 이름을 수정한다.")
    @Test
    void 회원의_이름을_수정한다() {
        // given
        MemberResponse 매트 = memberService.save(매트());

        String 패트_이름 = "패트";
        MemberUpdateRequest 매트_수정_요청 = new MemberUpdateRequest(패트_이름);

        // when
        memberService.update(매트.getId(), 매트_수정_요청);

        // then
        MemberResponse actual = memberService.findById(매트.getId());
        assertThat(actual.getDisplayName()).isEqualTo(패트_이름);
    }

    @DisplayName("회원을 제거한다.")
    @Test
    void 회원을_제거한다() {
        // given
        MemberResponse 후디 = memberService.save(후디());

        // when
        memberService.deleteById(후디.getId());

        // then
        assertThatThrownBy(() -> memberService.findById(후디.getId()))
                .isInstanceOf(NoSuchMemberException.class);
    }
}

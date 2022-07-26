package com.allog.dallog.member.service;

import static com.allog.dallog.common.fixtures.MemberFixtures.리버_이메일;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이메일;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberServiceTest {

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

    @DisplayName("이메일로 회원을 찾는다.")
    @Test
    void 이메일로_회원을_찾는다() {
        // given
        MemberResponse 파랑 = memberService.save(파랑());

        // when
        Member actual = memberService.getByEmail(파랑_이메일);

        // then
        assertThat(actual.getId()).isEqualTo(파랑.getId());
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

    @DisplayName("주어진 이메일로 가입된 회원이 있으면 true를 반환한다.")
    @Test
    void 주어진_이메일로_가입된_회원이_있으면_true를_반환한다() {
        // given
        memberService.save(파랑());

        // when
        boolean actual = memberService.existsByEmail(파랑_이메일);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("주어진 이메일로 가입된 회원이 없으면 false를 반환한다.")
    @Test
    void 주어진_이메일로_가입된_회원이_없으면_false를_반환한다() {
        // given
        memberService.save(파랑());

        // when
        boolean actual = memberService.existsByEmail(리버_이메일);

        // then
        assertThat(actual).isFalse();
    }
}

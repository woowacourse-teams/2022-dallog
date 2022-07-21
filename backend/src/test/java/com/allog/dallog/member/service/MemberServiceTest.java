package com.allog.dallog.member.service;

import static com.allog.dallog.common.fixtures.MemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.MemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.MemberFixtures.PROFILE_IMAGE_URI;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
        // given
        Member member = new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE);

        // when
        MemberResponse actual = memberService.save(member);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("이메일로 회원을 찾는다.")
    @Test
    void 이메일로_회원을_찾는다() {
        // given
        Member member = new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE);
        MemberResponse savedMember = memberService.save(member);

        // when
        Member foundMember = memberService.getByEmail(EMAIL);

        // then
        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
    }

    @DisplayName("id를 통해 회원을 단건 조회한다.")
    @Test
    void id를_통해_회원을_단건_조회한다() {
        // given
        MemberResponse member = memberService.save(
                new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));

        // when & then
        assertThat(memberService.findById(member.getId()))
                .usingRecursiveComparison()
                .isEqualTo(member);
    }

    @DisplayName("주어진 이메일로 가입된 회원이 있는지 확인한다.")
    @CsvSource(value = {"registerd@gmail.com,true", "notregistered@naver.com,false"})
    @ParameterizedTest
    void 주어진_이메일로_가입된_회원이_있는지_확인한다(String input, boolean expected) {
        // given
        String email = "registerd@gmail.com";
        Member member = new Member(email, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE);
        memberService.save(member);

        // when
        boolean actual = memberService.existsByEmail(input);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

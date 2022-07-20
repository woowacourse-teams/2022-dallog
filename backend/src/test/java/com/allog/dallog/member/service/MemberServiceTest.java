package com.allog.dallog.member.service;

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

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("회원을 저장한다.")
    @Test
    void 회원을_저장한다() {
        // given
        Member member = new Member("devhudi@gmail.com", "/image.png", "후디", SocialType.GOOGLE);

        // when
        MemberResponse actual = memberService.save(member);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("이메일로 회원을 찾는다.")
    @Test
    void 이메일로_회원을_찾는다() {
        // given
        String email = "newmember@gmail.com";
        String profileImageUrl = "/image.png";
        String displayName = "후디";
        Member member = new Member(email, profileImageUrl, displayName, SocialType.GOOGLE);
        MemberResponse savedMember = memberService.save(member);

        // when
        Member foundMember = memberService.findByEmail(email);

        // then
        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
    }

    @DisplayName("주어진 이메일로 가입된 회원이 있는지 확인한다.")
    @CsvSource(value = {"registerd@gmail.com,true", "notregistered@naver.com,false"})
    @ParameterizedTest
    void 주어진_이메일로_가입된_회원이_있는지_확인한다(String input, boolean expected) {
        // given
        String email = "registerd@gmail.com";
        String profileImageUrl = "/image.png";
        String displayName = "후디";
        Member member = new Member(email, profileImageUrl, displayName, SocialType.GOOGLE);
        memberService.save(member);

        // when
        boolean actual = memberService.existsByEmail(input);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

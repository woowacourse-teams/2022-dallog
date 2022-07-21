package com.allog.dallog.member.domain;

import static com.allog.dallog.common.fixtures.MemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.MemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.MemberFixtures.PROFILE_IMAGE_URI;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));
    }

    @DisplayName("이메일을 통해 회원을 찾는다.")
    @Test
    void 이메일을_통해_회원을_찾는다() {
        // given & when
        Member foundMember = memberRepository.findByEmail(EMAIL).get();

        // then
        assertThat(member.getId()).isEqualTo(foundMember.getId());
    }

    @DisplayName("중복된 이메일이 존재하는 경우 true를 반환한다.")
    @Test
    void 중복된_이메일이_존재하는_경우_true를_반환한다() {
        // given & when & then
        assertThat(memberRepository.existsByEmail(EMAIL)).isTrue();
    }
}

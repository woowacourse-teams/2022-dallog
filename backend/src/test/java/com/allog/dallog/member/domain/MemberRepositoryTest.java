package com.allog.dallog.member.domain;

import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이메일;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.common.annotation.RepositoryTest;
import com.allog.dallog.member.exception.NoSuchMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("중복된 이메일이 존재하는 경우 true를 반환한다.")
    @Test
    void 중복된_이메일이_존재하는_경우_true를_반환한다() {
        // given
        memberRepository.save(파랑());

        // when & then
        assertThat(memberRepository.existsByEmail(파랑_이메일)).isTrue();
    }

    @DisplayName("이메일을 통해 회원을 찾는다.")
    @Test
    void 이메일을_통해_회원을_찾는다() {
        // given
        Member 파랑 = memberRepository.save(파랑());

        // when
        Member actual = memberRepository.getByEmail(파랑_이메일);

        // then
        assertThat(actual.getId()).isEqualTo(파랑.getId());
    }

    @DisplayName("존재하지 않는 email을 조회할 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_email을_조회할_경우_예외를_던진다() {
        // given
        String email = "dev.hyeonic@gmail.com";

        // given & when & then
        assertThatThrownBy(() -> memberRepository.getByEmail(email))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("존재하지 않는 id이면 예외를 던진다.")
    @Test
    void 존재하지_않는_id이면_예외를_던진다() {
        // given
        Long id = 0L;

        // when & then
        assertThatThrownBy(() -> memberRepository.validateExistsById(id))
                .isInstanceOf(NoSuchMemberException.class);
    }
}

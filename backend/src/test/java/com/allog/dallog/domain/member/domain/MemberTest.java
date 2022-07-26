package com.allog.dallog.domain.member.domain;

import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_이메일;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑_프로필;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.SocialType;
import com.allog.dallog.domain.member.exception.InvalidMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @DisplayName("회원을 생성한다.")
    @Test
    void 회원을_생성한다() {
        // given & when & then
        assertDoesNotThrow(() -> new Member(파랑_이메일, 파랑_프로필, 파랑_이름, SocialType.GOOGLE));
    }

    @DisplayName("회원의 email이 형식이 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"dev.hyeonic@", "dev.hyeonicgmail.com", "dev.hyeonic@gmail", "@gmail.com", "dev.hyeonic"})
    void 회원의_email이_형식이_맞지_않으면_예외를_던진다(final String email) {
        // given & when & then
        assertThatThrownBy(() -> new Member(email, 파랑_프로필, 파랑_이름, SocialType.GOOGLE))
                .isInstanceOf(InvalidMemberException.class);
    }

    @DisplayName("회원의 이름이 1 ~ 10 사이가 아닌 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "일이삼사오육칠팔구십일"})
    void 회원의_이름이_1_에서_10_사이가_아닌_경우_예외를_던진다(final String displayName) {
        // given & when & then
        assertThatThrownBy(() -> new Member(파랑_이메일, 파랑_프로필, displayName, SocialType.GOOGLE))
                .isInstanceOf(InvalidMemberException.class);
    }
}

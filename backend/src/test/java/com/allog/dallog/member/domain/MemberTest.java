package com.allog.dallog.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.member.exception.InvalidMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @DisplayName("회원을 생성한다.")
    @Test
    void 회원을_생성한다() {
        // given
        String email = "dev.hyeonic@gmail.com";
        String profileImageUrl = "https://avatars.githubusercontent.com/u/59357153?v=4";
        String displayName = "매트";
        SocialType socialType = SocialType.GOOGLE;

        // when & then
        assertDoesNotThrow(() -> new Member(email, profileImageUrl, displayName, socialType));
    }

    @DisplayName("회원의 email이 형식이 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"dev.hyeonic@", "dev.hyeonicgmail.com", "dev.hyeonic@gmail", "@gmail.com", "dev.hyeonic"})
    void 회원의_email이_형식이_맞지_않으면_예외를_던진다(final String email) {
        // given
        String profileImageUrl = "https://avatars.githubusercontent.com/u/59357153?v=4";
        String displayName = "매트";
        SocialType socialType = SocialType.GOOGLE;

        // when & then
        assertThatThrownBy(() -> new Member(email, profileImageUrl, displayName, socialType))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }

    @DisplayName("회원의 이름이 1 ~ 10 사이가 아닌 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "일이삼사오육칠팔구십일"})
    void 회원의_이름이_1_에서_10_사이가_아닌_경우_예외를_던진다(final String displayName) {
        // given
        String email = "dev.hyeonic@gmail.com";
        String profileImageUrl = "https://avatars.githubusercontent.com/u/59357153?v=4";
        SocialType socialType = SocialType.GOOGLE;

        // when & then
        assertThatThrownBy(() -> new Member(email, profileImageUrl, displayName, socialType))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage("이름 형식이 올바르지 않습니다.");
    }
}

package com.allog.dallog.auth.service;

import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.CODE;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.common.config.TestConfig;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(classes = TestConfig.class)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("구글 로그인을 위한 링크를 생성한다.")
    @Test
    void 구글_로그인을_위한_링크를_생성한다() {
        // given
        String link = authService.generateGoogleLink();

        // when & then
        assertThat(link).isNotEmpty();
    }

    @DisplayName("토큰 생성을 하면 OAuth 서버에서 인증 후 토큰을 반환한다")
    @Test
    void 토큰_생성을_하면_OAuth_서버에서_인증_후_토큰을_반환한다() {
        // given & when
        TokenResponse actual = authService.generateTokenWithCode(CODE);

        // then
        assertThat(actual.getAccessToken()).isNotEmpty();
    }

    @DisplayName("Authorization Code를 받으면 회원이 데이터베이스에 저장된다.")
    @Test
    void Authorization_Code를_받으면_회원이_데이터베이스에_저장된다() {
        // given
        authService.generateTokenWithCode(CODE);

        // when & then
        assertThat(memberRepository.existsByEmail(OAUTH_EMAIL)).isTrue();
        // SutbOAuthClient가 반환하는 OAuthMember의 이메일
    }

    @DisplayName("이미 가입된 회원에 대한 Authorization Code를 전달받으면 추가로 유저가 생성되지 않는다")
    @Test
    void 이미_가입된_회원에_대한_Authorization_Code를_전달받으면_추가로_유저가_생성되지_않는다() {
        // 이미 가입된 유저가 소셜 로그인 버튼을 클릭했을 경우엔 회원가입 과정이 생략되고, 곧바로 access token이 발급되어야 한다.

        // given
        authService.generateTokenWithCode(CODE);

        // when & then
        authService.generateTokenWithCode(CODE);
        List<Member> actual = memberRepository.findAll();

        // then
        assertThat(actual).hasSize(1);
    }
}

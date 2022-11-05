package com.allog.dallog.domain.auth.application;

import static com.allog.dallog.common.Constants.MEMBER_REFRESH_TOKEN;
import static com.allog.dallog.common.Constants.MEMBER_이름;
import static com.allog.dallog.common.Constants.MEMBER_이메일;
import static com.allog.dallog.common.Constants.MEMBER_프로필_URL;
import static com.allog.dallog.domain.member.domain.SocialType.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.domain.TokenRepository;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.request.TokenRenewalRequest;
import com.allog.dallog.domain.auth.dto.response.AccessAndRefreshTokenResponse;
import com.allog.dallog.domain.auth.dto.response.AccessTokenResponse;
import com.allog.dallog.domain.auth.exception.InvalidTokenException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthServiceTest extends ServiceTest {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryRoleRepository categoryRoleRepository;

    @Autowired
    public AuthServiceTest(final AuthService authService,
                           final TokenProvider tokenProvider,
                           final TokenRepository tokenRepository,
                           final MemberRepository memberRepository,
                           final CategoryRepository categoryRepository,
                           final CategoryRoleRepository categoryRoleRepository) {
        this.authService = authService;
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.categoryRoleRepository = categoryRoleRepository;
    }

    private final OAuthMember oAuthMember = new OAuthMember(MEMBER_이메일, MEMBER_이름, MEMBER_프로필_URL,
            MEMBER_REFRESH_TOKEN);
    private User 회원;

    @BeforeEach
    void setUp() {
        회원 = new User();
        tokenRepository.deleteAll();
    }

    @DisplayName("회원을 등록한다.")
    @Test
    void 회원을_등록한다() {
        // given & when
        authService.generateAccessAndRefreshToken(oAuthMember);

        // then
        assertThat(memberRepository.existsByEmail(MEMBER_이메일)).isTrue();
    }

    @DisplayName("이미 가입한 회원은 등록하지 않고 로그인 한다.")
    @Test
    void 이미_가입한_회원은_등록하지_않고_로그인_한다() {
        // given
        authService.generateAccessAndRefreshToken(oAuthMember);

        // when
        authService.generateAccessAndRefreshToken(oAuthMember);

        // then
        List<Member> actual = memberRepository.findAll();
        assertThat(actual).hasSize(1);
    }

    @DisplayName("회원가입 또는 로그인 하면 토큰을 발급한다.")
    @Test
    void 회원가입_또는_로그인_하면_토큰을_발급한다() {
        // given & when
        AccessAndRefreshTokenResponse actual = authService.generateAccessAndRefreshToken(oAuthMember);

        // then
        assertAll(() -> {
            assertThat(actual.getAccessToken()).isNotEmpty();
            assertThat(actual.getRefreshToken()).isNotEmpty();
        });
    }

    @DisplayName("회원 등록 할 때 개인 카테고리도 등록한다.")
    @Test
    void 회원_등록_할_때_개인_카테고리도_등록한다() {
        // given & when
        authService.generateAccessAndRefreshToken(oAuthMember);

        // then
        Member 회원 = memberRepository.findByEmail(MEMBER_이메일).get();
        List<Category> 회원_카테고리들 = categoryRepository.findByMemberId(회원.getId());

        assertThat(회원_카테고리들).hasSize(1);
    }

    @DisplayName("회원 등록 할 때 개인 카테고리에 ADMIN 권한을 등록한다.")
    @Test
    void 회원_등록_시_회원의_개인_카태고리에_ADMIN_권한을_등록한다() {
        // given & when
        authService.generateAccessAndRefreshToken(oAuthMember);

        // then
        Member 회원 = memberRepository.findByEmail(MEMBER_이메일).get();
        List<CategoryRole> 회원_카테고리_권한들 = categoryRoleRepository.findByMemberId(회원.getId());

        assertThat(회원_카테고리_권한들).hasSize(1);
    }

    @DisplayName("회원의 토큰을 재발급 한다.")
    @Test
    void 회원의_토큰을_재발급_한다() {
        // given
        회원.회원_가입을_한다(MEMBER_이메일, MEMBER_이름, MEMBER_프로필_URL)
                .리프레시_토큰을_발급받는다();

        TokenRenewalRequest 토큰_재발급_요청 = new TokenRenewalRequest(회원.리프레시_토큰());

        // when
        AccessTokenResponse actual = authService.generateAccessToken(토큰_재발급_요청);

        // then
        assertThat(actual.getAccessToken()).isNotEmpty();
    }

    @DisplayName("회원의 토큰을 재발급 할 때 등록된 토큰이 아니면 예외를 발생한다.")
    @Test
    void 회원의_토큰을_재발급_할_때_등록된_토큰이_아니면_예외를_발생한다() {
        // given
        String 등록되지_않은_토큰 = "DummyRefreshToken";
        TokenRenewalRequest 토큰_재발급_요청 = new TokenRenewalRequest(등록되지_않은_토큰);

        // when & then
        assertThatThrownBy(() -> authService.generateAccessToken(토큰_재발급_요청))
                .isInstanceOf(InvalidTokenException.class);
    }

    class User {

        private Member member;

        User() {

        }

        public User 회원_가입을_한다(final String email, final String name, final String profile) {
            this.member = new Member(email, name, profile, GOOGLE);
            memberRepository.save(member);
            return this;
        }

        public void 리프레시_토큰을_발급받는다() {
            String payload = String.valueOf(member.getId());
            String refreshToken = tokenProvider.createRefreshToken(payload);
            tokenRepository.save(member.getId(), refreshToken);
        }

        public String 리프레시_토큰() {
            return tokenRepository.getToken(member.getId());
        }
    }
}

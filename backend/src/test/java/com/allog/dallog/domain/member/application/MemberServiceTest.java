package com.allog.dallog.domain.member.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.fixtures.AuthFixtures;
import com.allog.dallog.common.fixtures.SubscriptionFixtures;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.request.MemberUpdateRequest;
import com.allog.dallog.domain.member.dto.response.MemberResponse;
import com.allog.dallog.domain.member.dto.response.SubscribersResponse;
import com.allog.dallog.domain.member.exception.InvalidMemberException;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends ServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Test
    void 탈퇴한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        categoryService.save(관리자.getId(), 내_일정_생성_요청);

        // when
        memberService.deleteById(관리자.getId());

        Member 재가입_관리자 = memberRepository.save(관리자());
        categoryService.save(재가입_관리자.getId(), 내_일정_생성_요청);
        memberService.deleteById(재가입_관리자.getId());
    }

    @DisplayName("id를 통해 회원을 단건 조회한다.")
    @Test
    void id를_통해_회원을_단건_조회한다() {
        // given
        Long 파랑_id = parseMemberId(AuthFixtures.파랑_인증_코드_토큰_요청());

        // when & then
        assertThat(memberService.findById(파랑_id).getId())
                .isEqualTo(파랑_id);
    }

    @DisplayName("구독 id를 기반으로 member 정보를 조회한다.")
    @Test
    void 구독_id를_기반으로_member_정보를_조회한다() {
        // given
        Long 매트_id = parseMemberId(AuthFixtures.매트_인증_코드_토큰_요청());
        Member 매트 = memberRepository.getById(매트_id);

        Category BE_일정 = categoryRepository.save(BE_일정(매트));
        Subscription 색상_1_BE_일정_구독 = subscriptionRepository.save(SubscriptionFixtures.색상1_구독(매트, BE_일정));

        // when
        MemberResponse actual = memberService.findBySubscriptionId(색상_1_BE_일정_구독.getId());

        // then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(매트.getId());
            assertThat(actual.getEmail()).isEqualTo(매트.getEmail());
            assertThat(actual.getDisplayName()).isEqualTo(매트.getDisplayName());
            assertThat(actual.getProfileImageUrl()).isEqualTo(매트.getProfileImageUrl());
            assertThat(actual.getSocialType()).isEqualTo(매트.getSocialType());
        });
    }

    @DisplayName("회원의 이름을 수정한다.")
    @Test
    void 회원의_이름을_수정한다() {
        // given
        Long 매트_id = parseMemberId(AuthFixtures.매트_인증_코드_토큰_요청());

        String 패트_이름 = "패트";
        MemberUpdateRequest 매트_수정_요청 = new MemberUpdateRequest(패트_이름);

        // when
        memberService.update(매트_id, 매트_수정_요청);

        // then
        MemberResponse actual = memberService.findById(매트_id);
        assertThat(actual.getDisplayName()).isEqualTo(패트_이름);
    }

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    void 회원_탈퇴를_한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 내_일정 = categoryRepository.save(내_일정(관리자));
        CategoryRole 역할 = categoryRoleRepository.save(new CategoryRole(내_일정, 관리자, CategoryRoleType.ADMIN));
        Subscription 내_일정_구독 = subscriptionRepository.save(new Subscription(관리자, 내_일정, Color.COLOR_1));

        // when
        memberService.deleteById(관리자.getId());

        // then
        assertThatThrownBy(() -> memberService.findById(관리자.getId()))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("회원 탈퇴를 할 때, 다른 회원이 만든 카테고리를 구독만 하면 정상적으로 탈퇴한다.")
    @Test
    void 회원_탈퇴를_할_때_다른_회원이_만든_카테고리를_구독만_하면_정상적으로_탈퇴한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 리버 = memberRepository.save(리버());

        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        CategoryRole 역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 관리자, CategoryRoleType.ADMIN));
        Subscription 공통_일정_구독 = subscriptionRepository.save(new Subscription(관리자, 공통_일정, Color.COLOR_1));

        CategoryRole 리버_역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 리버, CategoryRoleType.NONE));
        Subscription 리버_공통_일정_구독 = subscriptionRepository.save(new Subscription(리버, 공통_일정, Color.COLOR_1));

        // when
        memberService.deleteById(리버.getId());

        // then
        assertThatThrownBy(() -> memberService.findById(리버.getId()))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("회원 탈퇴를 할 뗴, 구독한 카테고리의 유일한 ADMIN 계쩡이면 예외를 던진다.")
    @Test
    void 회원_탈퇴를_할_때_구독한_카테고리의_유일한_ADMIN_계정이면_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        CategoryRole 역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 관리자, CategoryRoleType.ADMIN));
        Subscription 색상1_구독 = subscriptionRepository.save(new Subscription(관리자, 공통_일정, Color.COLOR_1));

        // when & then
        assertThatThrownBy(() -> memberService.deleteById(관리자.getId()))
                .isInstanceOf(InvalidMemberException.class);
    }

    @DisplayName("회원 탈퇴를 할 뗴, 구독한 카테고리의 ADMIN이 더 있으면 정상적으로 탈퇴한다.")
    @Test
    void 회원_탈퇴를_할_때_구독한_카테고리의_ADMIN이_더_있으면_정상적으로_탈퇴한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        CategoryRole 역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 관리자, CategoryRoleType.ADMIN));
        Subscription 공통_일정_구독 = subscriptionRepository.save(new Subscription(관리자, 공통_일정, Color.COLOR_1));

        Member 리버 = memberRepository.save(리버());
        CategoryRole 리버_역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 리버, CategoryRoleType.ADMIN));
        Subscription 리버_공통_일정_구독 = subscriptionRepository.save(new Subscription(리버, 공통_일정, Color.COLOR_1));

        // when
        memberService.deleteById(관리자.getId());

        // then
        assertThatThrownBy(() -> memberService.findById(관리자.getId()))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("회원 탈퇴를 할 뗴, 구독한 카테고리의 ADMIN이 더 있고 NONE인 구독 카테고리가 있어도 정상적으로 탈퇴한다.")
    @Test
    void 회원_탈퇴를_할_때_구독한_카테고리의_ADMIN이_더_있고_NONE인_구독_카테고리가_있어도_정상적으로_탈퇴한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        CategoryRole 관리자_역할1 = categoryRoleRepository.save(new CategoryRole(공통_일정, 관리자, CategoryRoleType.ADMIN));
        Subscription 공통_일정_구독 = subscriptionRepository.save(new Subscription(관리자, 공통_일정, Color.COLOR_1));

        Category BE_일정 = categoryRepository.save(BE_일정(관리자));
        CategoryRole 관리자_역할2 = categoryRoleRepository.save(new CategoryRole(BE_일정, 관리자, CategoryRoleType.ADMIN));
        Subscription BE_일정_구독 = subscriptionRepository.save(new Subscription(관리자, BE_일정, Color.COLOR_1));

        Member 리버 = memberRepository.save(리버());
        CategoryRole 리버_역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 리버, CategoryRoleType.ADMIN));
        Subscription 리버_공통_일정_구독 = subscriptionRepository.save(new Subscription(리버, 공통_일정, Color.COLOR_1));

        // when & then
        assertThatThrownBy(() -> memberService.deleteById(관리자.getId()))
                .isInstanceOf(InvalidMemberException.class);
    }

    @DisplayName("회원 탈퇴를 할 때, 내가 만든 카테고리를 구독만 해도 정상적으로 탈퇴한다.")
    @Test
    void 회원_탈퇴를_할_때_내가_만든_카테고리를_구독만_해도_정상적으로_탈퇴한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        CategoryRole 역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 관리자, CategoryRoleType.NONE));
        Subscription 공통_일정_구독 = subscriptionRepository.save(new Subscription(관리자, 공통_일정, Color.COLOR_1));

        Member 리버 = memberRepository.save(리버());
        CategoryRole 리버_역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 리버, CategoryRoleType.ADMIN));
        Subscription 리버_공통_일정_구독 = subscriptionRepository.save(new Subscription(리버, 공통_일정, Color.COLOR_1));

        // when
        memberService.deleteById(관리자.getId());

        // then
        assertThatThrownBy(() -> memberService.findById(관리자.getId()))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("회원 탈퇴를 할 때, 내가 만든 카테고리를 구독해제 해도 정상적으로 탈퇴한다.")
    @Test
    void 회원_탈퇴를_할_때_내가_만든_카테고리를_구독해제_해도_정상적으로_탈퇴한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        Member 리버 = memberRepository.save(리버());
        CategoryRole 리버_역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 리버, CategoryRoleType.ADMIN));
        Subscription 리버_공통_일정_구독 = subscriptionRepository.save(new Subscription(리버, 공통_일정, Color.COLOR_1));

        // when
        memberService.deleteById(관리자.getId());

        // then
        assertThatThrownBy(() -> memberService.findById(관리자.getId()))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("회원 탈퇴를 할 때, 외부연동 카테고리가 있어도 정상적으로 탈퇴한다.")
    @Test
    void 회원_탈퇴를_할_때_외부연동_카테고리가_있어도_정상적으로_탈퇴한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 우아한테크코스_일정 = categoryRepository.save(우아한테크코스_일정(관리자));
        CategoryRole 리버_역할 = categoryRoleRepository.save(new CategoryRole(우아한테크코스_일정, 관리자, CategoryRoleType.ADMIN));
        Subscription 리버_우테코_일정_구독 = subscriptionRepository.save(new Subscription(관리자, 우아한테크코스_일정, Color.COLOR_1));

        // when
        memberService.deleteById(관리자.getId());

        // then
        assertThatThrownBy(() -> memberService.findById(관리자.getId()))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("특정 카테고리의 구독자 목록을 반환한다.")
    @Test
    void 특정_카테고리의_구독자_목록을_반환한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 후디 = memberRepository.save(후디());
        Member 리버 = memberRepository.save(리버());
        memberRepository.save(파랑());
        memberRepository.save(매트());

        CategoryResponse 카테고리 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        subscriptionService.save(후디.getId(), 카테고리.getId());
        subscriptionService.save(리버.getId(), 카테고리.getId());

        // when
        SubscribersResponse actual = memberService.findSubscribers(관리자.getId(), 카테고리.getId());

        // then
        assertAll(() -> {
            assertThat(actual.getSubscribers().size()).isEqualTo(3);
            assertThat(
                    actual.getSubscribers().stream().map(it -> it.getMember().getDisplayName())
                            .collect(Collectors.toList()))
                    .containsExactly(관리자_이름, 후디_이름, 리버_이름);
        });
    }

    @DisplayName("특정 카테고리의 구독자 목록을 ADMIN이 아닌 유저가 호출하면 예외가 발생한다.")
    @Test
    void 특정_카테고리의_구독자_목록을_ADMIN이_아닌_유저가_호출하면_예외가_발생한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 후디 = memberRepository.save(후디());

        CategoryResponse 카테고리 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        subscriptionService.save(후디.getId(), 카테고리.getId());

        // when & then
        assertThatThrownBy(() -> memberService.findSubscribers(후디.getId(), 카테고리.getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }
}

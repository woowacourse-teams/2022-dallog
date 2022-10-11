package com.allog.dallog.domain.member.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.더미_리프레시_토큰;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
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
import com.allog.dallog.domain.auth.domain.OAuthToken;
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

    @DisplayName("회원을 삭제한다.")
    @Test
    void 회원을_삭제한다() {
        // given
        Long 후디_id = parseMemberId(AuthFixtures.후디_인증_코드_토큰_요청());

        // when
        memberService.deleteById(후디_id);

        // then
        assertThatThrownBy(() -> memberService.findById(후디_id))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("회원을 삭제할때, 관련된 데이터도 모두 삭제한다.")
    @Test
    void 회원을_삭제할때_관련된_데이터도_모두_삭제한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        CategoryRole 역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 관리자, CategoryRoleType.ADMIN));
        Subscription 색상1_구독 = subscriptionRepository.save(new Subscription(관리자, 공통_일정, Color.COLOR_1));
        OAuthToken 외부_토큰 = oAuthTokenRepository.save(new OAuthToken(관리자, 더미_리프레시_토큰));

        // when
        memberService.deleteById(관리자.getId());

        // then
        assertThatThrownBy(() -> memberService.findById(관리자.getId()))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @DisplayName("회원 삭제 시 연관된 카테고리 역할 엔티티도 모두 제거된다.")
    @Test
    void 회원_삭제_시_연관된_카테고리_역할_엔티티도_모두_제거된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 후디 = memberRepository.save(후디());

        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        CategoryRole 역할 = categoryRoleRepository.save(new CategoryRole(공통_일정, 후디, CategoryRoleType.ADMIN));

        // when
        memberService.deleteById(후디.getId());
        boolean actual = categoryRoleRepository.findById(역할.getId()).isPresent();

        // then
        assertThat(actual).isFalse();
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

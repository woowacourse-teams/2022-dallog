package com.allog.dallog.domain.member.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.fixtures.AuthFixtures;
import com.allog.dallog.common.fixtures.SubscriptionFixtures;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.categoryrole.CategoryRole;
import com.allog.dallog.domain.categoryrole.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.CategoryRoleType;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.member.dto.MemberUpdateRequest;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

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

    @DisplayName("회원을 제거한다.")
    @Test
    void 회원을_제거한다() {
        // given
        Long 후디_id = parseMemberId(AuthFixtures.후디_인증_코드_토큰_요청());

        // when
        memberService.deleteById(후디_id);

        // then
        assertThatThrownBy(() -> memberService.findById(후디_id))
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
}

package com.allog.dallog.domain.member.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디_이름;
import static com.allog.dallog.common.fixtures.OAuthFixtures.매트;
import static com.allog.dallog.common.fixtures.OAuthFixtures.파랑;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.request.MemberUpdateRequest;
import com.allog.dallog.domain.member.dto.response.MemberResponse;
import com.allog.dallog.domain.member.dto.response.SubscribersResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
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
    private SubscriptionService subscriptionService;

    @Autowired
    private CategoryService categoryService;

    @DisplayName("id를 통해 회원을 단건 조회한다.")
    @Test
    void id를_통해_회원을_단건_조회한다() {
        // given
        Long 파랑_id = toMemberId(파랑.getOAuthMember());

        // when & then
        assertThat(memberService.findById(파랑_id).getId())
                .isEqualTo(파랑_id);
    }

    @DisplayName("회원의 이름을 수정한다.")
    @Test
    void 회원의_이름을_수정한다() {
        // given
        Long 매트_id = toMemberId(매트.getOAuthMember());

        String 패트_이름 = "패트";
        MemberUpdateRequest 매트_수정_요청 = new MemberUpdateRequest(패트_이름);

        // when
        memberService.update(매트_id, 매트_수정_요청);

        // then
        MemberResponse actual = memberService.findById(매트_id);
        assertThat(actual.getDisplayName()).isEqualTo(패트_이름);
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

    @DisplayName("특정 카테고리의 구독자 목록을 ADMIN이 아닌 회원이 호출하면 예외가 발생한다.")
    @Test
    void 특정_카테고리의_구독자_목록을_ADMIN이_아닌_회원이_호출하면_예외가_발생한다() {
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

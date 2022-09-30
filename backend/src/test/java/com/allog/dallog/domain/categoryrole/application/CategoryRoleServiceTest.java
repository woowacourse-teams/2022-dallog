package com.allog.dallog.domain.categoryrole.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.dto.request.CategoryRoleUpdateRequest;
import com.allog.dallog.domain.categoryrole.exception.NoPermissionToManageRoleException;
import com.allog.dallog.domain.categoryrole.exception.NotAbleToMangeRoleException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryRoleServiceTest extends ServiceTest {

    @Autowired
    private CategoryRoleService categoryRoleService;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @DisplayName("ADMIN 회원이 구독자의 역할을 변경할 수 있다")
    @Test
    void ADMIN_회원이_구독자의_역할을_변경할_수_있다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 후디 = memberRepository.save(후디());

        CategoryResponse BE_일정 = categoryService.save(관리자.getId(), BE_일정_생성_요청);

        subscriptionService.save(후디.getId(), BE_일정.getId());

        // when
        CategoryRoleUpdateRequest request = new CategoryRoleUpdateRequest(ADMIN);
        categoryRoleService.updateRole(관리자.getId(), 후디.getId(), BE_일정.getId(), request);

        CategoryRole actual = categoryRoleRepository.findByMemberIdAndCategoryId(후디.getId(), BE_일정.getId()).get();

        // then
        assertThat(actual.getCategoryRoleType()).isEqualTo(ADMIN);
    }

    @DisplayName("ADMIN 유저가 아닌 경우 구독자의 역할을 변경할 수 없다")
    @Test
    void ADMIN_유저가_아닌_경우_구독자의_역할을_변경할_수_없다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 관리자가_아닌_유저 = memberRepository.save(매트());
        Member 구독자 = memberRepository.save(후디());

        CategoryResponse BE_일정 = categoryService.save(관리자.getId(), BE_일정_생성_요청);

        subscriptionService.save(관리자가_아닌_유저.getId(), BE_일정.getId());
        subscriptionService.save(구독자.getId(), BE_일정.getId());

        CategoryRoleUpdateRequest request = new CategoryRoleUpdateRequest(ADMIN);

        // when & then
        assertThatThrownBy(
                () -> categoryRoleService.updateRole(관리자가_아닌_유저.getId(), 구독자.getId(), BE_일정.getId(), request))
                .isInstanceOf(NoPermissionToManageRoleException.class);
    }

    @DisplayName("ADMIN 회원이 다른 관리자 유저의 역할을 변경할 수 있다")
    @Test
    void ADMIN_회원이_다른_관리자_유저의_역할을_변경할_수_있다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse BE_일정 = categoryService.save(관리자.getId(), BE_일정_생성_요청);

        Member 후디 = memberRepository.save(후디());
        Member 매트 = memberRepository.save(매트());

        subscriptionService.save(후디.getId(), BE_일정.getId());
        subscriptionService.save(매트.getId(), BE_일정.getId());

        categoryRoleService.updateRole(관리자.getId(), 후디.getId(), BE_일정.getId(), new CategoryRoleUpdateRequest(ADMIN));
        categoryRoleService.updateRole(관리자.getId(), 매트.getId(), BE_일정.getId(), new CategoryRoleUpdateRequest(ADMIN));

        // when
        categoryRoleService.updateRole(후디.getId(), 매트.getId(), BE_일정.getId(), new CategoryRoleUpdateRequest(NONE));
        CategoryRole actual = categoryRoleRepository.findByMemberIdAndCategoryId(매트.getId(), BE_일정.getId()).get();

        // then
        assertThat(actual.getCategoryRoleType()).isEqualTo(NONE);
    }

    @DisplayName("ADMIN 회원이 자기자신의 역할을 변경할 수 있다.")
    @Test
    void ADMIN_회원이_자기자신의_역할을_변경할_수_있다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse BE_일정 = categoryService.save(관리자.getId(), BE_일정_생성_요청);

        Member 후디 = memberRepository.save(후디());
        subscriptionService.save(후디.getId(), BE_일정.getId());
        categoryRoleService.updateRole(관리자.getId(), 후디.getId(), BE_일정.getId(), new CategoryRoleUpdateRequest(ADMIN));
        // '관리자' 회원이 유일한 ADMIN이 아니도록 다른 ADMIN 추가

        // when
        CategoryRoleUpdateRequest request = new CategoryRoleUpdateRequest(NONE);
        categoryRoleService.updateRole(관리자.getId(), 관리자.getId(), BE_일정.getId(), request);

        CategoryRole actual = categoryRoleRepository.findByMemberIdAndCategoryId(관리자.getId(), BE_일정.getId()).get();

        // then
        assertThat(actual.getCategoryRoleType()).isEqualTo(NONE);
    }

    @DisplayName("ADMIN 회원이 자기자신의 역할을 변경할때, 자신이 유일한 ADMIN인 경우 예외가 발생한다.")
    @Test
    void ADMIN_회원이_자기자신의_역할을_변경할때_자신이_유일한_ADMIN인_경우_예외가_발생한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse BE_일정 = categoryService.save(관리자.getId(), BE_일정_생성_요청);

        // when & then
        CategoryRoleUpdateRequest request = new CategoryRoleUpdateRequest(NONE);
        assertThatThrownBy(() -> categoryRoleService.updateRole(관리자.getId(), 관리자.getId(), BE_일정.getId(), request))
                .isInstanceOf(NotAbleToMangeRoleException.class);
    }
}

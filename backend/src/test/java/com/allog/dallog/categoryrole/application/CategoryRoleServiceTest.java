package com.allog.dallog.categoryrole.application;

import static com.allog.dallog.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.category.domain.CategoryType.PERSONAL;
import static com.allog.dallog.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.categoryrole.domain.CategoryRoleType.NONE;
import static com.allog.dallog.common.Constants.개인_카테고리_이름;
import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static com.allog.dallog.common.Constants.외부_카테고리_이름;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.Constants.티거_이름;
import static com.allog.dallog.common.Constants.티거_이메일;
import static com.allog.dallog.common.Constants.티거_프로필_URL;
import static com.allog.dallog.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.category.domain.CategoryType;
import com.allog.dallog.categoryrole.domain.CategoryRole;
import com.allog.dallog.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.categoryrole.dto.request.CategoryRoleUpdateRequest;
import com.allog.dallog.categoryrole.dto.response.SubscribersResponse;
import com.allog.dallog.categoryrole.exception.ManagingCategoryLimitExcessException;
import com.allog.dallog.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.categoryrole.exception.NotAbleToChangeRoleException;
import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.subscription.domain.Subscription;
import com.allog.dallog.subscription.domain.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class CategoryRoleServiceTest extends ServiceTest {

    private final CategoryRoleUpdateRequest 카테고리_관리권한_부여_요청 = new CategoryRoleUpdateRequest(ADMIN);
    private final CategoryRoleUpdateRequest 카테고리_관리권한_해제_요청 = new CategoryRoleUpdateRequest(NONE);

    @Autowired
    private CategoryRoleService categoryRoleService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private IntegrationLogicBuilder 나인;
    private IntegrationLogicBuilder 티거;

    @BeforeEach
    void setUp() {
        나인 = new IntegrationLogicBuilder();
        티거 = new IntegrationLogicBuilder();
    }

    @Test
    void 카테고리의_구독자_목록을_반환한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        // when
        SubscribersResponse actual = categoryRoleService.findSubscribers(나인.회원().getId(), 나인.카테고리().getId());

        // then
        assertThat(actual.getSubscribers().size()).isEqualTo(2);
    }

    @Test
    void 관리자_권한이_아닌_회원이_카테고리_구독자_목록을_조회하면_예외가_발생한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        // when & then
        assertThatThrownBy(() -> categoryRoleService.findSubscribers(티거.회원().getId(), 나인.카테고리().getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Test
    void 관리자_역할로_변경하려는_회원이_이미_50개_이상의_카테고리에_관리자_권한이_있으면_예외가_발생한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());
        for (int i = 0; i < 50; i++) {
            티거.카테고리를_생성한다("카테고리 " + i, NORMAL);
        }

        // when & then
        assertThatThrownBy(
                () -> categoryRoleService.updateRole(
                        나인.회원().getId(), 티거.회원().getId(), 나인.카테고리().getId(), 카테고리_관리권한_부여_요청))
                .isInstanceOf(ManagingCategoryLimitExcessException.class);
    }

    @Transactional
    @Test
    void 카테고리_관리권한이_최고_관리자인_회원이_다른_최고_관리자의_권한을_변경한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        나인.내_카테고리_관리_권한을_부여한다(티거.회원());

        // when
        categoryRoleService.updateRole(티거.회원().getId(), 나인.회원().getId(), 나인.카테고리().getId(), 카테고리_관리권한_해제_요청);

        // then
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(나인.회원().getId(), 나인.카테고리().getId());
        assertThat(actual.getCategoryRoleType()).isEqualTo(NONE);
    }

    @Transactional
    @Test
    void 카테고리_관리권한이_최고_관리자인_회원이_구독자의_권한을_변경한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        // when
        categoryRoleService.updateRole(나인.회원().getId(), 티거.회원().getId(), 나인.카테고리().getId(), 카테고리_관리권한_부여_요청);
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(티거.회원().getId(), 나인.카테고리().getId());

        // then
        assertThat(actual.getCategoryRoleType()).isEqualTo(ADMIN);
    }

    @Transactional
    @Test
    void 카테고리_관리권한이_최고_관리자인_회원이_자신의_관리자_권한을_변경한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        나인.내_카테고리_관리_권한을_부여한다(티거.회원());

        // when
        categoryRoleService.updateRole(나인.회원().getId(), 나인.회원().getId(), 나인.카테고리().getId(), 카테고리_관리권한_해제_요청);

        // then
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(나인.회원().getId(), 나인.카테고리().getId());
        assertThat(actual.getCategoryRoleType()).isEqualTo(NONE);
    }

    @Test
    void 카테고리_관리권한이_없는_회원이_다른_회원의_권한을_변경하려하면_예외가_발생한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        // when & then
        assertThatThrownBy(
                () -> categoryRoleService.updateRole(
                        티거.회원().getId(), 나인.회원().getId(), 나인.카테고리().getId(), 카테고리_관리권한_부여_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Test
    void 유일한_카테고리_최고_관리자인_회원이_자신의_관리자_권한을_변경하려_하면_예외가_발생한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        // when & then
        assertThatThrownBy(() -> categoryRoleService.updateRole(
                나인.회원().getId(), 나인.회원().getId(), 나인.카테고리().getId(), 카테고리_관리권한_부여_요청))
                .isInstanceOf(NotAbleToChangeRoleException.class);
    }

    @DisplayName("개인 카테고리에 대한 회원의 역할을 변경할 경우 예외가 발생한다.")
    @Test
    void 개인_카테고리에_대한_회원의_권한을_변경하려_하면_예외가_발생한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(개인_카테고리_이름, PERSONAL);

        // when & then
        assertThatThrownBy(() -> categoryRoleService.updateRole(
                나인.회원().getId(), 나인.회원().getId(), 나인.카테고리().getId(), 카테고리_관리권한_부여_요청))
                .isInstanceOf(NotAbleToChangeRoleException.class);
    }

    @Test
    void 외부_카테고리에_대한_회원의_권한을_변경하려_하면_예외가_발생한다() {
        // given
        나인.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_생성한다(외부_카테고리_이름, GOOGLE);

        // when & then
        assertThatThrownBy(() -> categoryRoleService.updateRole(
                나인.회원().getId(), 나인.회원().getId(), 나인.카테고리().getId(), 카테고리_관리권한_부여_요청))
                .isInstanceOf(NotAbleToChangeRoleException.class);
    }

    private final class IntegrationLogicBuilder {

        private Member member;
        private Category category;
        private CategoryRole categoryRole;
        private Subscription subscription;

        private IntegrationLogicBuilder 회원_가입을_한다(final String email, final String name, final String profile) {
            Member member = new Member(email, name, profile, SocialType.GOOGLE);
            this.member = memberRepository.save(member);
            return this;
        }

        private IntegrationLogicBuilder 카테고리를_생성한다(final String categoryName, final CategoryType categoryType) {
            Category category = new Category(categoryName, this.member, categoryType);
            CategoryRole categoryRole = new CategoryRole(category, this.member, ADMIN);
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            this.category = categoryRepository.save(category);
            this.categoryRole = categoryRoleRepository.save(categoryRole);
            this.subscription = subscriptionRepository.save(subscription);
            return this;
        }

        private IntegrationLogicBuilder 카테고리를_구독한다(final Category category) {
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            CategoryRole categoryRole = new CategoryRole(category, this.member, NONE);
            this.subscription = subscriptionRepository.save(subscription);
            this.categoryRole = categoryRoleRepository.save(categoryRole);
            return this;
        }

        private IntegrationLogicBuilder 내_카테고리_관리_권한을_부여한다(final Member otherMember) {
            CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(otherMember.getId(),
                    category.getId());
            categoryRole.changeRole(ADMIN);
            categoryRoleRepository.save(categoryRole);
            return this;
        }

        private Member 회원() {
            return member;
        }

        private Category 카테고리() {
            return category;
        }
    }
}

package com.allog.dallog.domain.categoryrole.application;

import static com.allog.dallog.common.Constants.MEMBER_이름;
import static com.allog.dallog.common.Constants.MEMBER_이메일;
import static com.allog.dallog.common.Constants.MEMBER_프로필_URL;
import static com.allog.dallog.common.Constants.개인_카테고리_이름;
import static com.allog.dallog.common.Constants.네오_이름;
import static com.allog.dallog.common.Constants.네오_이메일;
import static com.allog.dallog.common.Constants.네오_프로필_URL;
import static com.allog.dallog.common.Constants.외부_카테고리_이름;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.Constants.포비_이름;
import static com.allog.dallog.common.Constants.포비_이메일;
import static com.allog.dallog.common.Constants.포비_프로필_URL;
import static com.allog.dallog.domain.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.common.annotation.IntegrationTest;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.dto.request.CategoryRoleUpdateRequest;
import com.allog.dallog.domain.categoryrole.exception.ManagingCategoryLimitExcessException;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.categoryrole.exception.NotAbleToChangeRoleException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryRoleServiceTest extends IntegrationTest {

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

    private User 제이슨;
    private User 네오;
    private User 포비;

    @BeforeEach
    void setUp() {
        제이슨 = new User();
        네오 = new User();
        포비 = new User();
    }

    @DisplayName("ADMIN 회원이 구독자의 역할을 변경할 수 있다")
    @Test
    void ADMIN_회원이_구독자의_역할을_변경할_수_있다() {
        // given
        제이슨.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        네오.회원_가입을_한다(MEMBER_이메일, MEMBER_이름, MEMBER_프로필_URL)
                .카테고리를_구독한다(제이슨.카테고리());

        // when
        categoryRoleService.updateRole(제이슨.계정().getId(), 네오.계정().getId(), 제이슨.카테고리().getId(), 카테고리_관리권한_부여_요청);

        // then
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(
                제이슨.계정().getId(), 제이슨.카테고리().getId());

        assertThat(actual.getCategoryRoleType()).isEqualTo(ADMIN);
    }

    @DisplayName("관리자(NONE 이상) 역할로 변경하려는 대상이 이미 50개 이상의 카테고리에서 관리자로 참여하고 있는 경우 예외가 발생한다.")
    @Test
    void 관리자_역할로_변경하려는_대상이_이미_50개_이상의_카테고리에서_관리자로_참여하고_있는_경우_예외가_발생한다() {
        // given
        제이슨.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        네오.회원_가입을_한다(MEMBER_이메일, MEMBER_이름, MEMBER_프로필_URL)
                .카테고리를_구독한다(제이슨.카테고리());
        for (int i = 0; i < 50; i++) {
            네오.카테고리를_등록한다("카테고리 " + i, NORMAL);
        }

        // when & then
        assertThatThrownBy(
                () -> categoryRoleService.updateRole(
                        제이슨.계정().getId(), 네오.계정().getId(), 제이슨.카테고리().getId(), 카테고리_관리권한_부여_요청))
                .isInstanceOf(ManagingCategoryLimitExcessException.class);
    }

    @DisplayName("ADMIN 회원이 아닌 경우 구독자의 역할을 변경할 수 없다")
    @Test
    void ADMIN_회원이_아닌_경우_구독자의_역할을_변경할_수_없다() {
        // given
        제이슨.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        네오.회원_가입을_한다(MEMBER_이메일, MEMBER_이름, MEMBER_프로필_URL)
                .카테고리를_구독한다(제이슨.카테고리());

        // when & then
        assertThatThrownBy(
                () -> categoryRoleService.updateRole(
                        네오.계정().getId(), 제이슨.계정().getId(), 제이슨.카테고리().getId(), 카테고리_관리권한_부여_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @DisplayName("ADMIN 회원이 다른 관리자 회원의 역할을 변경할 수 있다")
    @Test
    void ADMIN_회원이_다른_관리자_회원의_역할을_변경할_수_있다() {
        // given
        제이슨.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        네오.회원_가입을_한다(MEMBER_이메일, MEMBER_이름, MEMBER_프로필_URL)
                .카테고리를_구독한다(제이슨.카테고리());

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(제이슨.카테고리());

        제이슨.내_카테고리_관리_권한을_부여한다(네오.계정());

        // when
        categoryRoleService.updateRole(네오.계정().getId(), 제이슨.계정().getId(), 제이슨.카테고리().getId(), 카테고리_관리권한_해제_요청);

        // then
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(제이슨.계정().getId(), 제이슨.카테고리().getId());
        assertThat(actual.getCategoryRoleType()).isEqualTo(NONE);
    }

    @DisplayName("ADMIN 회원이 자기자신의 역할을 변경할 수 있다.")
    @Test
    void ADMIN_회원이_자기자신의_역할을_변경할_수_있다() {
        // given
        제이슨.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        네오.회원_가입을_한다(MEMBER_이메일, MEMBER_이름, MEMBER_프로필_URL)
                .카테고리를_구독한다(제이슨.카테고리());

        제이슨.내_카테고리_관리_권한을_부여한다(네오.계정());

        // when
        categoryRoleService.updateRole(제이슨.계정().getId(), 제이슨.계정().getId(), 제이슨.카테고리().getId(), 카테고리_관리권한_해제_요청);

        // then
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(제이슨.계정().getId(), 제이슨.카테고리().getId());
        assertThat(actual.getCategoryRoleType()).isEqualTo(NONE);
    }

    @DisplayName("ADMIN 회원이 자기자신의 역할을 변경할때, 자신이 유일한 ADMIN인 경우 예외가 발생한다.")
    @Test
    void ADMIN_회원이_자기자신의_역할을_변경할때_자신이_유일한_ADMIN인_경우_예외가_발생한다() {
        // given
        제이슨.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when & then
        assertThatThrownBy(() -> categoryRoleService.updateRole(
                제이슨.계정().getId(), 제이슨.계정().getId(), 제이슨.카테고리().getId(), 카테고리_관리권한_해제_요청))
                .isInstanceOf(NotAbleToChangeRoleException.class);
    }

    @DisplayName("개인 카테고리에 대한 회원의 역할을 변경할 경우 예외가 발생한다.")
    @Test
    void 개인_카테고리에_대한_회원의_역할을_변경할_경우_예외가_발생한다() {
        // given
        제이슨.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(개인_카테고리_이름, PERSONAL);

        // when & then
        assertThatThrownBy(() -> categoryRoleService.updateRole(
                제이슨.계정().getId(), 제이슨.계정().getId(), 제이슨.카테고리().getId(), 카테고리_관리권한_해제_요청))
                .isInstanceOf(NotAbleToChangeRoleException.class);
    }

    @DisplayName("외부 카테고리에 대한 회원의 역할을 변경할 경우 예외가 발생한다.")
    @Test
    void 외부_카테고리에_대한_회원의_역할을_변경할_경우_예외가_발생한다() {
        // given
        제이슨.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(외부_카테고리_이름, GOOGLE);

        // when & then
        assertThatThrownBy(() -> categoryRoleService.updateRole(
                제이슨.계정().getId(), 제이슨.계정().getId(), 제이슨.카테고리().getId(), 카테고리_관리권한_해제_요청))
                .isInstanceOf(NotAbleToChangeRoleException.class);
    }

    private final class User {

        private Member member;
        private Category category;

        private User 회원_가입을_한다(final String email, final String name, final String profile) {
            this.member = new Member(email, name, profile, SocialType.GOOGLE);
            memberRepository.save(member);
            return this;
        }

        private User 카테고리를_등록한다(final String categoryName, final CategoryType categoryType) {
            this.category = new Category(categoryName, this.member, categoryType);
            CategoryRole categoryRole = new CategoryRole(this.category, this.member, ADMIN);
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            categoryRepository.save(category);
            categoryRoleRepository.save(categoryRole);
            subscriptionRepository.save(subscription);
            return this;
        }

        private User 카테고리를_구독한다(final Category category) {
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            CategoryRole categoryRole = new CategoryRole(category, this.member, NONE);
            subscriptionRepository.save(subscription);
            categoryRoleRepository.save(categoryRole);
            return this;
        }

        private User 내_카테고리_관리_권한을_부여한다(final Member otherMember) {
            CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(otherMember.getId(),
                    category.getId());
            categoryRole.changeRole(ADMIN);
            return this;
        }


        private Member 계정() {
            return member;
        }

        private Category 카테고리() {
            return category;
        }
    }
}

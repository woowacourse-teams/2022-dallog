package com.allog.dallog.subscription.application;

import static com.allog.dallog.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.category.domain.CategoryType.PERSONAL;
import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static com.allog.dallog.common.Constants.스터디_카테고리_이름;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.Constants.티거_이름;
import static com.allog.dallog.common.Constants.티거_이메일;
import static com.allog.dallog.common.Constants.티거_프로필_URL;
import static com.allog.dallog.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.auth.exception.NoPermissionException;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.categoryrole.domain.CategoryRole;
import com.allog.dallog.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.categoryrole.exception.NoSuchCategoryRoleException;
import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.builder.GivenBuilder;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.subscription.domain.Subscription;
import com.allog.dallog.subscription.domain.SubscriptionRepository;
import com.allog.dallog.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.subscription.exception.NoSuchSubscriptionException;
import com.allog.dallog.subscription.exception.NotAbleToUnsubscribeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class SubscriptionServiceTest extends ServiceTest {

    private final SubscriptionUpdateRequest 구독_정보_변경_요청 = new SubscriptionUpdateRequest(COLOR_1, true);

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Test
    void 구독을_생성한다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        GivenBuilder 티거 = 티거();

        // when
        SubscriptionResponse response = subscriptionService.save(티거.회원().getId(), 나인.카테고리().getId());

        // then
        assertThat(response.getCategory().getName()).isEqualTo(취업_카테고리_이름);
    }

    @Test
    void 타인의_개인_카테고리를_구독하려하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, PERSONAL);

        GivenBuilder 티거 = 티거();

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(티거.회원().getId(), 나인.카테고리().getId()))
                .isInstanceOf(NoPermissionException.class)
                .hasMessage("구독 권한이 없는 카테고리입니다.");
    }

    @Test
    void 이미_구독한_카테고리를_다시_구독하려하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, PERSONAL);

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(나인.회원().getId(), 나인.카테고리().getId()))
                .isInstanceOf(ExistSubscriptionException.class);
    }

    @Test
    void 단건_구독_정보를_조회한다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, PERSONAL);

        // when
        Subscription actual = subscriptionRepository.getById(나인.구독().getId());

        // then
        assertThat(actual.getCategory().getId()).isEqualTo(나인.카테고리().getId());
    }

    @Test
    void 회원의_구독_목록을_조회한다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        GivenBuilder 티거 = 티거().카테고리를_구독한다(나인.카테고리());

        나인.카테고리를_생성한다(스터디_카테고리_이름, NORMAL);
        티거.카테고리를_구독한다(나인.카테고리());

        // when
        SubscriptionsResponse actual = subscriptionService.findByMemberId(티거.회원().getId());

        // then
        assertThat(actual.getSubscriptions()).hasSize(2);
    }

    @Test
    void 구독_정보를_수정한다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        // when
        subscriptionService.update(나인.구독().getId(), 나인.회원().getId(), 구독_정보_변경_요청);

        // then
        Subscription actual = subscriptionRepository.getById(나인.구독().getId());
        assertAll(() -> {
            assertThat(actual.getColor()).isEqualTo(COLOR_1);
            assertThat(actual.isChecked()).isTrue();
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#", "#00FF00"})
    void 존재하지_않는_색상으로_구독_정보를_수정하려_하면_예외가_발생한다(final String colorCode) {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        SubscriptionUpdateRequest 잘못된_구독_변경_요청 = new SubscriptionUpdateRequest(colorCode, true);

        // when & then
        assertThatThrownBy(() -> subscriptionService.update(나인.구독().getId(), 나인.회원().getId(), 잘못된_구독_변경_요청))
                .isInstanceOf(InvalidSubscriptionException.class);
    }

    @Test
    void 구독_정보를_삭제한다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        GivenBuilder 티거 = 티거().카테고리를_구독한다(나인.카테고리());

        // when
        subscriptionService.delete(티거.구독().getId(), 티거.회원().getId());

        // then
        assertThatThrownBy(() -> subscriptionRepository.getById(티거.구독().getId()))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @Test
    void 자신의_구독_정보가_아닌_구독을_삭제할_경우_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        GivenBuilder 티거 = 티거().카테고리를_구독한다(나인.카테고리());

        // when & then
        assertThatThrownBy(() -> subscriptionService.delete(티거.구독().getId(), 나인.회원().getId()))
                .isInstanceOf(NoPermissionException.class);
    }

    @Transactional
    @Test
    void 카테고리를_구독하면_카테고리에_대한_구독자_권한이_생성된다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        GivenBuilder 티거 = 티거();

        // when
        subscriptionService.save(티거.회원().getId(), 나인.카테고리().getId());

        // then
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(티거.회원().getId(), 나인.카테고리().getId());
        assertThat(actual.getCategoryRoleType()).isEqualTo(CategoryRoleType.NONE);
    }

    @Transactional
    @Test
    void 카테고리를_구독_해제하면_카테고리에_대한_권한이_제거된다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        GivenBuilder 티거 = 티거().카테고리를_구독한다(나인.카테고리());

        // when
        subscriptionService.delete(티거.구독().getId(), 티거.회원().getId());

        // then
        assertThatThrownBy(() -> categoryRoleRepository.getByMemberIdAndCategoryId(티거.회원().getId(), 나인.카테고리().getId()))
                .isInstanceOf(NoSuchCategoryRoleException.class);
    }

    @Transactional
    @Test
    void 카테고리_권한이_관리자_일때_구독_해제를_하려하면_예외가_발생한다() {
        // given
        GivenBuilder 나인 = 나인().카테고리를_생성한다(취업_카테고리_이름, NORMAL);

        GivenBuilder 티거 = 티거().카테고리를_구독한다(나인.카테고리());

        나인.카테고리_관리_권한을_부여한다(티거.회원(), 나인.카테고리());

        // when & then
        assertThatThrownBy(() -> subscriptionService.delete(티거.구독().getId(), 티거.회원().getId()))
                .isInstanceOf(NotAbleToUnsubscribeException.class);
    }
}

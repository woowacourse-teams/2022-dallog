package com.allog.dallog.domain.subscription.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.관리자_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.리버_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.매트_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.파랑_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.후디_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.categoryrole.exception.NoSuchCategoryRoleException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NotAbleToUnsubscribeException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class SubscriptionServiceTest extends ServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @DisplayName("새로운 구독을 생성한다.")
    @Test
    void 새로운_구독을_생성한다() {
        // given
        Long 후디_id = toMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);

        Long 리버_id = toMemberId(리버_인증_코드_토큰_요청());

        // when
        SubscriptionResponse response = subscriptionService.save(리버_id, BE_일정.getId());

        // then
        assertThat(response.getCategory().getName()).isEqualTo(BE_일정_이름);
    }

    @DisplayName("자신이 생성하지 않은 개인 카테고리를 구독시 예외가 발생한다.")
    @Test
    void 자신이_생성하지_않은_개인_카테고리를_구독시_예외가_발생한다() {
        // given
        Long 후디_id = toMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse 후디_개인_학습_일정 = categoryService.save(후디_id, 내_일정_생성_요청);

        Long 매트_id = toMemberId(매트_인증_코드_토큰_요청());

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(매트_id, 후디_개인_학습_일정.getId()))
                .isInstanceOf(NoPermissionException.class)
                .hasMessage("구독 권한이 없는 카테고리입니다.");
    }

    @DisplayName("이미 존재하는 구독 정보를 저장할 경우 예외를 던진다.")
    @Test
    void 이미_존재하는_구독_정보를_저장할_경우_예외를_던진다() {
        // given
        Long 후디_id = toMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(후디_id, BE_일정.getId()))
                .isInstanceOf(ExistSubscriptionException.class);
    }

    @DisplayName("구독 id를 기반으로 단건 조회한다.")
    @Test
    void 구독_id를_기반으로_단건_조회한다() {
        // given
        Long 후디_id = toMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);

        Long 리버_id = toMemberId(리버_인증_코드_토큰_요청());
        SubscriptionResponse 빨간색_구독 = subscriptionService.save(리버_id, BE_일정.getId());

        // when
        SubscriptionResponse foundResponse = subscriptionService.findById(빨간색_구독.getId());

        // then
        assertAll(() -> {
            assertThat(foundResponse.getId()).isEqualTo(빨간색_구독.getId());
            assertThat(foundResponse.getCategory().getId()).isEqualTo(BE_일정.getId());
        });
    }

    @DisplayName("존재하지 않는 구독 정보인 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_구독_정보인_경우_예외를_던진다() {
        // given & when & then
        assertThatThrownBy(() -> subscriptionService.findById(0L))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @DisplayName("회원 정보를 기반으로 구독 정보를 조회한다.")
    @Test
    void 회원_정보를_기반으로_구독_정보를_조회한다() {
        // given
        Long 관리자_id = toMemberId(관리자_인증_코드_토큰_요청());
        Long 매트_id = toMemberId(매트_인증_코드_토큰_요청());
        Long 리버_id = toMemberId(리버_인증_코드_토큰_요청());
        CategoryResponse 공통_일정 = categoryService.save(관리자_id, 공통_일정_생성_요청);
        CategoryResponse BE_일정 = categoryService.save(매트_id, BE_일정_생성_요청);
        CategoryResponse FE_일정 = categoryService.save(리버_id, FE_일정_생성_요청);

        Long 후디_id = toMemberId(후디_인증_코드_토큰_요청());
        subscriptionService.save(후디_id, 공통_일정.getId());
        subscriptionService.save(후디_id, BE_일정.getId());
        subscriptionService.save(후디_id, FE_일정.getId());

        // when
        SubscriptionsResponse subscriptionsResponse = subscriptionService.findByMemberId(후디_id);

        // then
        // TODO: 개인 일정 구독 정보를 포함하여 3 + 1 = 4개, N + 1 문제 개선 예정
        assertThat(subscriptionsResponse.getSubscriptions()).hasSize(4);
    }

    @DisplayName("category id를 기반으로 구독 정보를 조회한다.")
    @Test
    void category_id를_기반으로_구독_정보를_조회한다() {
        // given
        Long 매트_id = toMemberId(매트_인증_코드_토큰_요청());
        Long 파랑_id = toMemberId(파랑_인증_코드_토큰_요청());
        Long 리버_id = toMemberId(리버_인증_코드_토큰_요청());
        Long 후디_id = toMemberId(후디_인증_코드_토큰_요청());

        CategoryResponse BE_일정 = categoryService.save(매트_id, BE_일정_생성_요청);
        subscriptionService.save(파랑_id, BE_일정.getId());
        subscriptionService.save(리버_id, BE_일정.getId());
        subscriptionService.save(후디_id, BE_일정.getId());

        // when
        List<SubscriptionResponse> actual = subscriptionService.findByCategoryId(BE_일정.getId());

        // then
        assertThat(actual).hasSize(4);
    }

    @DisplayName("구독 정보를 수정한다.")
    @Test
    void 구독_정보를_수정한다() {
        // given
        Long 후디_id = toMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);

        Long 리버_id = toMemberId(리버_인증_코드_토큰_요청());
        SubscriptionResponse response = subscriptionService.save(리버_id, BE_일정.getId());
        Color color = Color.COLOR_1;

        // when
        SubscriptionUpdateRequest request = new SubscriptionUpdateRequest(color, true);
        subscriptionService.update(response.getId(), 리버_id, request);

        // then
        assertAll(() -> {
            assertThat(request.getColor()).isEqualTo(color);
            assertThat(request.isChecked()).isTrue();
        });
    }

    @DisplayName("구독 정보 수정 시 존재하지 않는 색상인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#", "#00FF00"})
    void 구독_정보_수정_시_존재하지_않는_색상인_경우_예외를_던진다(final String colorCode) {
        // given
        Long 후디_id = toMemberId(후디_인증_코드_토큰_요청());
        CategoryResponse BE_일정 = categoryService.save(후디_id, BE_일정_생성_요청);

        Long 리버_id = toMemberId(리버_인증_코드_토큰_요청());
        SubscriptionResponse response = subscriptionService.save(리버_id, BE_일정.getId());

        // when
        SubscriptionUpdateRequest request = new SubscriptionUpdateRequest(colorCode, true);

        // then
        assertThatThrownBy(() -> subscriptionService.update(response.getId(), 리버_id, request))
                .isInstanceOf(InvalidSubscriptionException.class);
    }

    @DisplayName("구독 정보를 삭제한다.")
    @Test
    void 구독_정보를_삭제한다() {
        // given
        Long 관리자_id = toMemberId(관리자_인증_코드_토큰_요청());
        CategoryResponse 공통_일정 = categoryService.save(관리자_id, 공통_일정_생성_요청);
        CategoryResponse BE_일정 = categoryService.save(관리자_id, BE_일정_생성_요청);
        CategoryResponse FE_일정 = categoryService.save(관리자_id, FE_일정_생성_요청);

        Long 후디_id = toMemberId(후디_인증_코드_토큰_요청());
        SubscriptionResponse response = subscriptionService.save(후디_id, 공통_일정.getId());
        subscriptionService.save(후디_id, BE_일정.getId());
        subscriptionService.save(후디_id, FE_일정.getId());

        // when
        subscriptionService.delete(response.getId(), 후디_id);

        // then
        assertThat(subscriptionService.findByMemberId(후디_id).getSubscriptions()).hasSize(3);
    }

    @DisplayName("자신의 구독 정보가 아닌 구독을 삭제할 경우 예외를 던진다.")
    @Test
    void 자신의_구독_정보가_아닌_구독을_삭제할_경우_예외를_던진다() {
        // given
        Long 관리자_id = toMemberId(관리자_인증_코드_토큰_요청());
        Long 파랑_id = toMemberId(파랑_인증_코드_토큰_요청());

        CategoryResponse 공통_일정 = categoryService.save(관리자_id, 공통_일정_생성_요청);
        SubscriptionResponse 공통_일정_구독 = subscriptionService.save(파랑_id, 공통_일정.getId());

        // when & then
        assertThatThrownBy(() -> subscriptionService.delete(공통_일정_구독.getId(), 관리자_id))
                .isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("카테고리를 구독하면 카테고리에 대한 NONE 역할이 생성된다")
    @Test
    void 카테고리를_구독하면_카테고리에_대한_NONE_역할이_생성된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        // when
        Member 후디 = memberRepository.save(후디());
        subscriptionService.save(후디.getId(), 공통_일정.getId());

        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(후디.getId(), 공통_일정.getId());

        // then
        assertThat(actual.getCategoryRoleType()).isEqualTo(CategoryRoleType.NONE);
    }

    @DisplayName("카테고리를 구독 해제하면 카테고리에 대한 역할이 제거된다")
    @Test
    void 카테고리를_구독_해제하면_카테고리에_대한_역할이_제거된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        Member 후디 = memberRepository.save(후디());
        SubscriptionResponse 공통_일정_구독 = subscriptionService.save(후디.getId(), 공통_일정.getId());

        // when
        subscriptionService.delete(공통_일정_구독.getId(), 후디.getId());

        // then
        assertThatThrownBy(() -> categoryRoleRepository.getByMemberIdAndCategoryId(후디.getId(), 공통_일정.getId()))
                .isInstanceOf(NoSuchCategoryRoleException.class);
    }

    @Transactional
    @DisplayName("카테고리 역할이 NONE이 아닌 경우 카테고리를 구독 해제할 수 없다")
    @Test
    void 카테고리_역할이_NONE이_아닌_경우_카테고리를_구독_해제할_수_없다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        Member 후디 = memberRepository.save(후디());
        SubscriptionResponse 공통_일정_구독 = subscriptionService.save(후디.getId(), 공통_일정.getId());

        CategoryRole 역할 = categoryRoleRepository.getByMemberIdAndCategoryId(후디.getId(), 공통_일정.getId());
        역할.changeRole(CategoryRoleType.ADMIN);

        // when & then
        assertThatThrownBy(() -> subscriptionService.delete(공통_일정_구독.getId(), 후디.getId()))
                .isInstanceOf(NotAbleToUnsubscribeException.class);
    }
}

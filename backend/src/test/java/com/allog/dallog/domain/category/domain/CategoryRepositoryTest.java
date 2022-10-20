package com.allog.dallog.domain.category.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라;
import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상1_구독;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.RepositoryTest;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryRepositoryTest extends RepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @DisplayName("카테고리 제목과 타입을 통해 해당하는 카테고리를 조회한다.")
    @Test
    void 카테고리_제목과_타입을_통해_해당하는_카테고리를_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, 공통_일정));
        Category BE_일정 = categoryRepository.save(BE_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, BE_일정));
        Category FE_일정 = categoryRepository.save(FE_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, FE_일정));
        Category 매트_아고라 = categoryRepository.save(매트_아고라(관리자));
        subscriptionRepository.save(색상1_구독(관리자, 매트_아고라));
        Category 후디_JPA_스터디 = categoryRepository.save(후디_JPA_스터디(관리자));
        subscriptionRepository.save(색상1_구독(관리자, 후디_JPA_스터디));
        Category 내_일정 = categoryRepository.save(내_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, 내_일정));
        Category 우아한테크코스_일정 = categoryRepository.save(우아한테크코스_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, 우아한테크코스_일정));

        // when
        List<Category> actual = categoryRepository.findByCategoryTypeAndNameContaining(NORMAL, "일");

        // then
        assertThat(actual).hasSize(3)
                .extracting(Category::getName)
                .contains(공통_일정_이름, BE_일정_이름, FE_일정_이름);
    }

    @DisplayName("카테고리 이름 검색 결과가 존재하지 않는 경우 아무것도 조회 하지 않는다.")
    @Test
    void 카테고리_이름_검색_결과가_존재하지_않는_경우_아무것도_조회_하지_않는다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, 공통_일정));
        Category BE_일정 = categoryRepository.save(BE_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, BE_일정));
        Category FE_일정 = categoryRepository.save(FE_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, FE_일정));
        Category 매트_아고라 = categoryRepository.save(매트_아고라(관리자));
        subscriptionRepository.save(색상1_구독(관리자, 매트_아고라));

        // when
        List<Category> actual = categoryRepository.findByCategoryTypeAndNameContaining(NORMAL, "파랑");

        // then
        assertThat(actual).hasSize(0);
    }

    @DisplayName("구독자수가 많은 순서로 정렬하여 반환한다.")
    @Test
    void 구독자수가_많은_순서로_정렬하여_반환한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, 공통_일정));
        Category BE_일정 = categoryRepository.save(BE_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, BE_일정));
        Category FE_일정 = categoryRepository.save(FE_일정(관리자));
        subscriptionRepository.save(색상1_구독(관리자, FE_일정));

        Member 매트 = memberRepository.save(매트());
        Member 리버 = memberRepository.save(리버());
        Member 후디 = memberRepository.save(후디());
        Member 파랑 = memberRepository.save(파랑());

        subscriptionRepository.save(색상1_구독(매트, 공통_일정));
        subscriptionRepository.save(색상1_구독(리버, 공통_일정));
        subscriptionRepository.save(색상1_구독(후디, 공통_일정));
        subscriptionRepository.save(색상1_구독(파랑, 공통_일정));

        subscriptionRepository.save(색상1_구독(매트, BE_일정));
        subscriptionRepository.save(색상1_구독(리버, BE_일정));
        subscriptionRepository.save(색상1_구독(후디, BE_일정));

        // when
        List<Category> actual = categoryRepository.findByCategoryTypeAndNameContaining(NORMAL, "");

        // then
        assertThat(actual).hasSize(3)
                .containsExactlyInAnyOrder(공통_일정, BE_일정, FE_일정);
    }

    @DisplayName("member id와 categoryType을 기반으로 조회한다.")
    @Test
    void member_id와_categoryType을_기반으로_조회한다() {
        // given
        Member 매트 = memberRepository.save(매트());
        categoryRepository.save(공통_일정(매트));
        categoryRepository.save(BE_일정(매트));
        categoryRepository.save(FE_일정(매트));
        categoryRepository.save(매트_아고라(매트));
        categoryRepository.save(후디_JPA_스터디(매트));

        // when
        List<Category> actual = categoryRepository.findByMemberIdAndCategoryType(매트.getId(), NORMAL);

        // then
        assertThat(actual).hasSize(5);
    }

    @DisplayName("특정 회원이 생성한 카테고리를 조회한다.")
    @Test
    void 특정_회원이_생성한_카테고리를_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        categoryRepository.save(공통_일정(관리자));
        categoryRepository.save(BE_일정(관리자));
        categoryRepository.save(FE_일정(관리자));

        Member 후디 = memberRepository.save(후디());
        categoryRepository.save(후디_JPA_스터디(후디));

        // when
        List<Category> categories = categoryRepository.findByMemberId(관리자.getId());

        // then
        assertAll(() -> {
            assertThat(categories).hasSize(3)
                    .extracting(Category::getName)
                    .containsExactlyInAnyOrder(공통_일정_이름, BE_일정_이름, FE_일정_이름);
            assertThat(
                    categories.stream()
                            .map(Category::getCreatedAt)
                            .allMatch(Objects::nonNull))
                    .isTrue();
        });
    }

    @DisplayName("카테고리 id와 회원의 id가 모두 일치하는 카테고리가 있으면 true를 반환한다.")
    @Test
    void 카테고리_id와_회원의_id가_모두_일치하는_카테고리가_있으면_true를_반환한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        // when
        boolean actual = categoryRepository.existsByIdAndMemberId(공통_일정.getId(), 관리자.getId());

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("카테고리 id와 회원의 id가 모두 일치하는 카테고리가 없으면 false를 반환한다.")
    @Test
    void 카테고리_id와_회원의_id가_모두_일치하는_카테고리가_없으면_false를_반환한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        // when
        boolean actual = categoryRepository.existsByIdAndMemberId(공통_일정.getId(), 0L);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("특정 회원이 만든 카테고리를 모두 삭제한다")
    @Test
    void 특정_회원이_만든_카테고리를_모두_삭제한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        categoryRepository.save(공통_일정(관리자));
        categoryRepository.save(BE_일정(관리자));

        // when
        categoryRepository.deleteByMemberId(관리자.getId());

        // then
        assertThat(categoryRepository.findByMemberId(관리자.getId()))
                .hasSize(0);
    }
}

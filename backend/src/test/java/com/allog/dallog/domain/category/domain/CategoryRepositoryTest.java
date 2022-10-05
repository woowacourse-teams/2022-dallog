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
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상1_구독;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.RepositoryTest;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

class CategoryRepositoryTest extends RepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @DisplayName("카테고리 이름과 타입과 페이징을 활용하여 해당하는 카테고리를 조회한다.")
    @Test
    void 카테고리_이름과_타입과_페이징을_활용하여_해당하는_카테고리를_조회한다() {
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

        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Slice<Category> actual = categoryRepository.findByCategoryTypeAndNameContaining(NORMAL, "일", pageRequest);

        // then
        assertThat(actual.getContent()).hasSize(3)
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

        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        Slice<Category> actual = categoryRepository.findByCategoryTypeAndNameContaining(NORMAL, "파랑", pageRequest);

        // then
        assertThat(actual.getContent()).hasSize(0);
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

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        List<Category> actual = categoryRepository.findByCategoryTypeAndNameContaining(NORMAL, "", pageRequest)
                .getContent();

        // then
        assertThat(actual).hasSize(3)
                .containsExactlyInAnyOrder(공통_일정, BE_일정, FE_일정);
    }

    @DisplayName("특정 멤버가 생성한 카테고리를 카테고리 이름과 페이징을 통해 조회한다.")
    @Test
    void 특정_멤버가_생성한_카테고리를_카테고리_이름과_페이징을_통해_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        categoryRepository.save(공통_일정(관리자));
        categoryRepository.save(BE_일정(관리자));
        categoryRepository.save(FE_일정(관리자));
        categoryRepository.save(매트_아고라(관리자));
        categoryRepository.save(후디_JPA_스터디(관리자));

        Member 후디 = memberRepository.save(후디());
        categoryRepository.save(후디_JPA_스터디(후디));

        PageRequest pageRequest = PageRequest.of(0, 8);

        // when
        Slice<Category> categories = categoryRepository.findByMemberIdAndNameContaining(관리자.getId(), "일", pageRequest);

        // then
        assertAll(() -> {
            assertThat(categories.getContent()).hasSize(3)
                    .extracting(Category::getName)
                    .containsExactlyInAnyOrder(공통_일정_이름, BE_일정_이름, FE_일정_이름);
            assertThat(
                    categories.getContent().stream()
                            .map(Category::getCreatedAt)
                            .allMatch(Objects::nonNull))
                    .isTrue();
        });
    }

    @DisplayName("특정 멤버가 생성한 카테고리를 조회한다.")
    @Test
    void 특정_멤버가_생성한_카테고리를_조회한다() {
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

    @DisplayName("멤버와 카테고리 역할 목록으로 카테고리를 조회한다.")
    @Test
    void 멤버와_카테고리_역할_목록으로_카테고리를_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 후디 = memberRepository.save(후디());

        Category 카테고리1 = categoryRepository.save(공통_일정(관리자));
        Category 카테고리2 = categoryRepository.save(BE_일정(관리자));
        Category 카테고리3 = categoryRepository.save(FE_일정(관리자));
        Category 카테고리4 = categoryRepository.save(매트_아고라(관리자));
        Category 카테고리5 = categoryRepository.save(후디_JPA_스터디(관리자));

        categoryRoleRepository.save(new CategoryRole(카테고리2, 관리자, ADMIN));
        categoryRoleRepository.save(new CategoryRole(카테고리4, 관리자, ADMIN));

        categoryRoleRepository.save(new CategoryRole(카테고리1, 후디, ADMIN));
        categoryRoleRepository.save(new CategoryRole(카테고리3, 후디, ADMIN));
        categoryRoleRepository.save(new CategoryRole(카테고리5, 후디, ADMIN));

        // when
        List<Category> categories = categoryRepository.findByMemberIdAndCategoryRoleTypes(후디.getId(), Set.of(ADMIN));
        List<String> actual = categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());

        // then
        assertThat(actual).containsExactly(공통_일정_이름, FE_일정_이름, 후디_JPA_스터디_이름);
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

        PageRequest pageRequest = PageRequest.of(0, 2);

        // when
        categoryRepository.deleteByMemberId(관리자.getId());

        // then
        assertThat(categoryRepository.findByMemberIdAndNameContaining(관리자.getId(), "", pageRequest))
                .hasSize(0);
    }
}

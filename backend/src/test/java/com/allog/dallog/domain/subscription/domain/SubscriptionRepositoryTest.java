package com.allog.dallog.domain.subscription.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.리버;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상1_구독;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상2_구독;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상3_구독;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.common.annotation.RepositoryTest;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SubscriptionRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @DisplayName("존재하지 않는 카테고리를 확인할 경우 true를 반환한다.")
    @Test
    void 존재하지_않는_카테고리를_확인할_경우_true를_반환한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        Member 매트 = memberRepository.save(매트());

        // when
        boolean actual = subscriptionRepository.existsByMemberIdAndCategoryId(매트.getId(), 공통_일정.getId());

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("이미 존재하는 카테고리를 확인할 경우 true를 반환한다.")
    @Test
    void 이미_존재하는_카테고리를_확인할_경우_true를_반환한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        Member 매트 = memberRepository.save(매트());
        subscriptionRepository.save(색상1_구독(매트, 공통_일정));

        // when
        boolean actual = subscriptionRepository.existsByMemberIdAndCategoryId(매트.getId(), 공통_일정.getId());

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("회원의 특정 구독 정보 여부를 확인한다.")
    @Test
    void 회원의_특정_구독_정보_여부를_확인한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        Member 후디 = memberRepository.save(후디());
        Subscription 색상1_구독 = 색상1_구독(후디, 공통_일정);
        subscriptionRepository.save(색상1_구독);

        // when
        boolean actual = subscriptionRepository.existsByIdAndMemberId(색상1_구독.getId(), 후디.getId());

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("회원의 존재하지 않는 구독 정보 여부를 확인한다.")
    @Test
    void 회원의_존재하지_않는_구독_정보_여부를_확인한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        // when
        boolean actual = subscriptionRepository.existsByIdAndMemberId(0L, 관리자.getId());

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("회원 정보를 기반으로 구독 정보를 조회한다.")
    @Test
    void 회원_정보를_기반으로_구독_정보를_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));
        Category BE_일정 = categoryRepository.save(BE_일정(관리자));
        Category FE_일정 = categoryRepository.save(FE_일정(관리자));

        Member 후디 = memberRepository.save(후디());
        subscriptionRepository.save(색상1_구독(후디, 공통_일정));
        subscriptionRepository.save(색상2_구독(후디, BE_일정));
        subscriptionRepository.save(색상3_구독(후디, FE_일정));

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(후디.getId());

        // then
        assertThat(subscriptions).hasSize(3);
    }

    @DisplayName("회원의 구독 정보가 존재하지 않는 경우 빈 리스트가 조회된다.")
    @Test
    void 회원의_구독_정보가_존재하지_않는_경우_빈_리스트가_조회된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(관리자.getId());

        // then
        assertThat(subscriptions).isEmpty();
    }

    @DisplayName("member id 리스트를 기반으로 구독 정보를 조회한다.")
    @Test
    void member_id_리스트를_기반으로_구독_정보를_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category 공통_일정 = categoryRepository.save(공통_일정(관리자));

        Member 매트 = memberRepository.save(매트());
        Member 리버 = memberRepository.save(리버());
        Member 파랑 = memberRepository.save(파랑());
        Member 후디 = memberRepository.save(후디());

        subscriptionRepository.save(색상1_구독(매트, 공통_일정));
        subscriptionRepository.save(색상1_구독(리버, 공통_일정));
        subscriptionRepository.save(색상1_구독(파랑, 공통_일정));
        subscriptionRepository.save(색상1_구독(후디, 공통_일정));

        List<Long> memberIds = Stream.of(매트, 리버, 파랑, 후디)
                .map(Member::getId)
                .collect(Collectors.toList());

        // when
        List<Subscription> actual = subscriptionRepository.findByMemberIdIn(memberIds);

        // then
        assertThat(actual).hasSize(4);
    }

    @DisplayName("member id 리스트가 비어있는 경우 빈 리스트를 반환한다.")
    @Test
    void member_id_리스트가_비어있는_경우_빈_리스트를_반환한다() {
        // given
        List<Long> memberIds = List.of();

        // when
        List<Subscription> actual = subscriptionRepository.findByMemberIdIn(memberIds);

        // then
        assertThat(actual).isEmpty();
    }

    @DisplayName("특정 카테고리들에 속한 구독을 전부 삭제한다")
    @Test
    void 특정_카테고리들에_속한_구독을_전부_삭제한다() {
        // given
        Member 관리자 = 관리자();
        memberRepository.save(관리자);
        Member 파랑 = 파랑();
        memberRepository.save(파랑);

        Category BE_일정 = BE_일정(관리자);
        Category FE_일정 = FE_일정(관리자);
        Category 공통_일정 = 공통_일정(관리자);
        categoryRepository.save(BE_일정);
        categoryRepository.save(FE_일정);
        categoryRepository.save(공통_일정);

        subscriptionRepository.save(색상1_구독(관리자, BE_일정));
        subscriptionRepository.save(색상2_구독(관리자, FE_일정));
        subscriptionRepository.save(색상3_구독(관리자, 공통_일정));
        subscriptionRepository.save(색상1_구독(파랑, BE_일정));
        subscriptionRepository.save(색상2_구독(파랑, FE_일정));
        subscriptionRepository.save(색상3_구독(파랑, 공통_일정));

        // when
        subscriptionRepository.deleteByCategoryIdIn(List.of(
                BE_일정.getId(), FE_일정.getId(), 공통_일정.getId()
        ));

        // then
        assertThat(subscriptionRepository.findAll()).hasSize(0);
    }

    @DisplayName("존재하지 않는 id인 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_id인_경우_예외를_던진다() {
        // given
        Long id = 0L;

        // when & then
        assertThatThrownBy(() -> subscriptionRepository.getById(id))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @DisplayName("특정 member가 특정 category를 구독한 경우 예외를 던진다.")
    @Test
    void 특정_member가_특정_category를_구독한_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Category BE_일정 = categoryRepository.save(BE_일정(관리자));
        Category FE_일정 = categoryRepository.save(FE_일정(관리자));

        // when
        Member 매트 = memberRepository.save(매트());
        subscriptionRepository.save(색상1_구독(매트, BE_일정)); // BE만 구독

        // then
        assertThatThrownBy(() ->
                subscriptionRepository.validateNotExistsByMemberIdAndCategoryId(매트.getId(), BE_일정.getId()))
                .isInstanceOf(ExistSubscriptionException.class);
    }

    @DisplayName("특정 구독 id가 특정 member의 구독이 아닌 경우 예외를 던진다.")
    @Test
    void 특정_구독_id가_특정_member의_구독이_아닌_경우_예외를_던진다() {
        // given
        Member 매트 = memberRepository.save(매트());

        // when & then
        assertThatThrownBy(() ->
                subscriptionRepository.validateExistsByIdAndMemberId(0L, 매트.getId()))
                .isInstanceOf(NoPermissionException.class);
    }
}

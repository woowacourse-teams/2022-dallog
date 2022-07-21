package com.allog.dallog.subscription.repository;

import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_1_NAME;
import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_2_NAME;
import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_3_NAME;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.DISPLAY_NAME2;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.EMAIL2;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.PROFILE_IMAGE_URI;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.PROFILE_IMAGE_URI2;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_BLUE;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_RED;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_YELLOW;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.global.config.JpaConfig;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.subscription.domain.Subscription;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
class SubscriptionRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @DisplayName("회원 정보를 기반으로 구독 정보를 조회한다.")
    @Test
    void 회원_정보를_기반으로_구독_정보를_조회한다() {
        // given
        Member creator = memberRepository.save(new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));
        Category category1 = categoryRepository.save(new Category(CATEGORY_1_NAME, creator));
        Category category2 = categoryRepository.save(new Category(CATEGORY_2_NAME, creator));
        Category category3 = categoryRepository.save(new Category(CATEGORY_3_NAME, creator));

        Member member = memberRepository.save(new Member(EMAIL2, PROFILE_IMAGE_URI2, DISPLAY_NAME2, SocialType.GOOGLE));
        Subscription subscription1 = new Subscription(member, category1, COLOR_RED);
        Subscription subscription2 = new Subscription(member, category2, COLOR_BLUE);
        Subscription subscription3 = new Subscription(member, category3, COLOR_YELLOW);

        subscriptionRepository.save(subscription1);
        subscriptionRepository.save(subscription2);
        subscriptionRepository.save(subscription3);

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(member.getId());

        // then
        assertThat(subscriptions).hasSize(3);
    }

    @DisplayName("회원의 구독 정보가 존재하지 않는 경우 빈 리스트가 조회된다.")
    @Test
    void 회원의_구독_정보가_존재하지_않는_경우_빈_리스트가_조회된다() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(member.getId());

        // then
        assertThat(subscriptions).isEmpty();
    }

    @DisplayName("회원의 특정 구독 정보 여부를 확인한다.")
    @Test
    void 회원의_특정_구독_정보_여부를_확인한다() {
        // given
        Member creator = memberRepository.save(new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));
        Category category1 = categoryRepository.save(new Category(CATEGORY_1_NAME, creator));

        Member member = memberRepository.save(new Member(EMAIL2, PROFILE_IMAGE_URI2, DISPLAY_NAME2, SocialType.GOOGLE));
        Subscription subscription1 = new Subscription(member, category1, COLOR_RED);

        subscriptionRepository.save(subscription1);

        // when
        boolean actual = subscriptionRepository.existsByIdAndMemberId(subscription1.getId(), member.getId());

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("회원의 존재하지 않는 구독 정보 여부를 확인한다.")
    @Test
    void 회원의_존재하지_않는_구독_정보_여부를_확인한다() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));

        // when
        boolean actual = subscriptionRepository.existsByIdAndMemberId(0L, member.getId());

        // then
        assertThat(actual).isFalse();
    }
}

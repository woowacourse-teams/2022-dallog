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
import org.junit.jupiter.api.BeforeEach;
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

    private Member creator;
    private Member member;

    private Category firstCategory;
    private Category secondCategory;
    private Category thirdCategory;

    @BeforeEach
    void setUp() {
        creator = memberRepository.save(new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));
        member = memberRepository.save(new Member(EMAIL2, PROFILE_IMAGE_URI2, DISPLAY_NAME2, SocialType.GOOGLE));

        firstCategory = categoryRepository.save(new Category(CATEGORY_1_NAME, creator));
        secondCategory = categoryRepository.save(new Category(CATEGORY_2_NAME, creator));
        thirdCategory = categoryRepository.save(new Category(CATEGORY_3_NAME, creator));
    }

    @DisplayName("회원 정보를 기반으로 구독 정보를 조회한다.")
    @Test
    void 회원_정보를_기반으로_구독_정보를_조회한다() {
        // given
        subscriptionRepository.save(new Subscription(member, firstCategory, COLOR_RED));
        subscriptionRepository.save(new Subscription(member, secondCategory, COLOR_BLUE));
        subscriptionRepository.save(new Subscription(member, thirdCategory, COLOR_YELLOW));

        // when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(member.getId());

        // then
        assertThat(subscriptions).hasSize(3);
    }

    @DisplayName("회원의 구독 정보가 존재하지 않는 경우 빈 리스트가 조회된다.")
    @Test
    void 회원의_구독_정보가_존재하지_않는_경우_빈_리스트가_조회된다() {
        // given & when
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(member.getId());

        // then
        assertThat(subscriptions).isEmpty();
    }

    @DisplayName("회원의 특정 구독 정보 여부를 확인한다.")
    @Test
    void 회원의_특정_구독_정보_여부를_확인한다() {
        // given
        Subscription subscription = subscriptionRepository.save(new Subscription(member, firstCategory, COLOR_RED));

        // when
        boolean actual = subscriptionRepository.existsByIdAndMemberId(subscription.getId(), member.getId());

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("회원의 존재하지 않는 구독 정보 여부를 확인한다.")
    @Test
    void 회원의_존재하지_않는_구독_정보_여부를_확인한다() {
        // given & when
        boolean actual = subscriptionRepository.existsByIdAndMemberId(0L, member.getId());

        // then
        assertThat(actual).isFalse();
    }
}

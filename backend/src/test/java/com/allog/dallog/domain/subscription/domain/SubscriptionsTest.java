package com.allog.dallog.domain.subscription.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.setId;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_일정;
import static com.allog.dallog.common.fixtures.IntegrationScheduleFixtures.달록_여행;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.subscription.domain.Color.COLOR_1;
import static com.allog.dallog.subscription.domain.Color.COLOR_2;
import static com.allog.dallog.subscription.domain.Color.COLOR_3;
import static com.allog.dallog.subscription.domain.Color.COLOR_4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.exception.NoSuchCategoryException;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.subscription.domain.Subscription;
import com.allog.dallog.subscription.domain.Subscriptions;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubscriptionsTest {

    @DisplayName("체크된 카테고리 중 내부 카테고리의 아이디를 찾는다.")
    @Test
    void 체크된_카테고리_중_내부_카테고리의_아이디를_찾는다() {
        // given
        Member 파랑 = 파랑();
        Category 공통_일정 = setId(공통_일정(파랑), 1L);
        Category BE_일정 = setId(BE_일정(파랑), 2L);
        Category 내_일정 = setId(내_일정(파랑), 3L);
        Category 우아한테크코스_일정 = setId(우아한테크코스_일정(파랑), 4L);

        Subscription 공통_일정_구독 = new Subscription(파랑, 공통_일정, COLOR_1);
        Subscription BE_일정_구독 = new Subscription(파랑, BE_일정, COLOR_2);
        Subscription 내_일정_구독 = new Subscription(파랑, 내_일정, COLOR_3);
        Subscription 우아한테크코스_일정_구독 = new Subscription(파랑, 우아한테크코스_일정, COLOR_4);

        BE_일정_구독.change(COLOR_2, false);

        Subscriptions subscriptions =
                new Subscriptions(List.of(공통_일정_구독, BE_일정_구독, 내_일정_구독, 우아한테크코스_일정_구독));

        // when & then
        assertThat(subscriptions.findInternalCategory()).isEqualTo(List.of(공통_일정, 내_일정));
    }

    @DisplayName("체크된 카테고리 중 외부 카테고리의 아이디를 찾는다.")
    @Test
    void 체크된_카테고리_중_외부_카테고리의_아이디를_찾는다() {
        // given
        Member 파랑 = 파랑();
        Category 공통_일정 = setId(공통_일정(파랑), 1L);
        Category BE_일정 = setId(BE_일정(파랑), 2L);
        Category 내_일정 = setId(내_일정(파랑), 3L);
        Category 우아한테크코스_일정 = setId(우아한테크코스_일정(파랑), 4L);

        Subscription 공통_일정_구독 = new Subscription(파랑, 공통_일정, COLOR_1);
        Subscription BE_일정_구독 = new Subscription(파랑, BE_일정, COLOR_2);
        Subscription 내_일정_구독 = new Subscription(파랑, 내_일정, COLOR_3);
        Subscription 우아한테크코스_일정_구독 = new Subscription(파랑, 우아한테크코스_일정, COLOR_4);

        Subscriptions subscriptions =
                new Subscriptions(List.of(공통_일정_구독, BE_일정_구독, 내_일정_구독, 우아한테크코스_일정_구독));

        // when & then
        assertThat(subscriptions.findExternalCategory()).isEqualTo(List.of(우아한테크코스_일정));
    }

    @DisplayName("특정 스케줄의 구독 색상을 찾는다.")
    @Test
    void 특정_스케줄의_구독_색상을_찾는다() {
        // given
        Member 파랑 = 파랑();
        Category 공통_일정 = setId(공통_일정(파랑), 1L);
        Category BE_일정 = setId(BE_일정(파랑), 2L);
        Category 내_일정 = setId(내_일정(파랑), 3L);
        Category 우아한테크코스_일정 = setId(우아한테크코스_일정(파랑), 4L);

        Subscription 공통_일정_구독 = new Subscription(파랑, 공통_일정, COLOR_1);
        Subscription BE_일정_구독 = new Subscription(파랑, BE_일정, COLOR_2);
        Subscription 내_일정_구독 = new Subscription(파랑, 내_일정, COLOR_3);
        Subscription 우아한테크코스_일정_구독 = new Subscription(파랑, 우아한테크코스_일정, COLOR_4);

        Subscriptions subscriptions =
                new Subscriptions(List.of(공통_일정_구독, BE_일정_구독, 내_일정_구독, 우아한테크코스_일정_구독));

        // when & then
        assertThat(subscriptions.findColor(달록_여행)).isEqualTo(COLOR_2);
    }

    @DisplayName("구독하지 않은 스케줄의 구독 색상을 찾는 경우 예외를 던진다")
    @Test
    void 구독하지_않은_스케줄의_구독_색상을_찾는_경우_예외를_던진다() {
        // given
        Member 파랑 = 파랑();
        Category 공통_일정 = setId(공통_일정(파랑), 1L);
        setId(BE_일정(파랑), 2L);

        Subscription 공통_일정_구독 = new Subscription(파랑, 공통_일정, COLOR_1);

        Subscriptions subscriptions = new Subscriptions(List.of(공통_일정_구독));

        // when & then
        assertThatThrownBy(() -> subscriptions.findColor(달록_여행)).isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("구독한 카테고리중 내부 카테고리를 찾아 반환한다.")
    @Test
    void 구독한_카테고리중_내부_카테고리를_찾아_반환한다() {
        // given
        Member 파랑 = 파랑();
        Category 공통_일정 = setId(공통_일정(파랑), 1L);
        setId(BE_일정(파랑), 2L);

        Subscription 공통_일정_구독 = new Subscription(파랑, 공통_일정, COLOR_1);

        Subscriptions subscriptions = new Subscriptions(List.of(공통_일정_구독));

        // when
        List<Category> categories = subscriptions.findInternalCategory();

        // then
        assertThat(categories).hasSize(1);
    }

    @DisplayName("구독한 카테고리중 외부 카테고리를 찾아 반환한다.")
    @Test
    void 구독한_카테고리중_외부_카테고리를_찾아_반환한다() {
        // given
        Member 파랑 = 파랑();
        Category 공통_일정 = setId(공통_일정(파랑), 1L);
        setId(BE_일정(파랑), 2L);

        Subscription 공통_일정_구독 = new Subscription(파랑, 공통_일정, COLOR_1);

        Subscriptions subscriptions = new Subscriptions(List.of(공통_일정_구독));

        // when
        List<Category> categories = subscriptions.findExternalCategory();

        // then
        assertThat(categories).hasSize(0);
    }
}

package com.allog.dallog.domain.subscription.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.setId;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정;
import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_1;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_2;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_3;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_4;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.member.domain.Member;
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
        assertThat(subscriptions.findCheckedCategoryIdsBy(Category::isInternal)).isEqualTo(List.of(1L, 3L));
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
        assertThat(subscriptions.findCheckedCategoryIdsBy(Category::isExternal)).isEqualTo(List.of(4L));
    }
}

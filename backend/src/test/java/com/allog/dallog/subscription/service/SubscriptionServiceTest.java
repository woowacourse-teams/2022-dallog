package com.allog.dallog.subscription.service;


import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_CREATE_REQUEST_1;
import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_CREATE_REQUEST_2;
import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_CREATE_REQUEST_3;
import static com.allog.dallog.common.fixtures.MemberFixtures.MEMBER;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.CREATE_REQUEST_BLUE;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.CREATE_REQUEST_RED;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.CREATE_REQUEST_YELLOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.auth.exception.NoPermissionException;
import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.category.service.CategoryService;
import com.allog.dallog.common.fixtures.MemberFixtures;
import com.allog.dallog.member.dto.MemberResponse;
import com.allog.dallog.member.service.MemberService;
import com.allog.dallog.subscription.dto.request.SubscriptionCreateRequest;
import com.allog.dallog.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.subscription.exception.NoSuchSubscriptionException;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class SubscriptionServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @DisplayName("????????? ????????? ????????????.")
    @Test
    void ?????????_?????????_????????????() {
        // given
        MemberResponse member = memberService.save(MEMBER);
        CategoryResponse categoryResponse = categoryService.save(member.getId(), new CategoryCreateRequest("BE ??????"));
        String color = "#ffffff";

        // when
        SubscriptionResponse response = subscriptionService.save(member.getId(), categoryResponse.getId(),
                new SubscriptionCreateRequest(color));

        // then
        assertAll(() -> {
            assertThat(response.getCategory().getName()).isEqualTo("BE ??????");
            assertThat(response.getColor()).isEqualTo(color);
        });
    }

    @DisplayName("??? ?????? ????????? ????????? ?????? ????????? ?????????.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#"})
    void ???_??????_?????????_?????????_??????_?????????_?????????(final String color) {
        // given
        MemberResponse member = memberService.save(MEMBER);
        CategoryResponse categoryResponse = categoryService.save(member.getId(), new CategoryCreateRequest("BE ??????"));

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(member.getId(), categoryResponse.getId(),
                new SubscriptionCreateRequest(color))).isInstanceOf(InvalidSubscriptionException.class);
    }

    @DisplayName("?????? id??? ???????????? ?????? ????????????.")
    @Test
    void ??????_id???_????????????_??????_????????????() {
        // given
        MemberResponse member = memberService.save(MEMBER);
        CategoryResponse categoryResponse = categoryService.save(member.getId(), new CategoryCreateRequest("BE ??????"));
        String color = "#ffffff";
        SubscriptionResponse subscriptionResponse = subscriptionService.save(member.getId(), categoryResponse.getId(),
                new SubscriptionCreateRequest(color));

        // when
        SubscriptionResponse foundResponse = subscriptionService.findById(subscriptionResponse.getId());

        // then
        assertAll(() -> {
            assertThat(foundResponse.getId()).isEqualTo(subscriptionResponse.getId());
            assertThat(foundResponse.getCategory().getId()).isEqualTo(categoryResponse.getId());
            assertThat(foundResponse.getColor()).isEqualTo(color);
        });
    }

    @DisplayName("???????????? ?????? ?????? ????????? ?????? ????????? ?????????.")
    @Test
    void ????????????_??????_??????_?????????_??????_?????????_?????????() {
        // given & when & then
        assertThatThrownBy(() -> subscriptionService.findById(0L))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @DisplayName("?????? ????????? ???????????? ?????? ????????? ????????????.")
    @Test
    void ??????_?????????_????????????_??????_?????????_????????????() {
        // given
        MemberResponse creator = memberService.save(MemberFixtures.CREATOR);
        CategoryResponse categoryResponse1 = categoryService.save(creator.getId(), CATEGORY_CREATE_REQUEST_1);
        CategoryResponse categoryResponse2 = categoryService.save(creator.getId(), CATEGORY_CREATE_REQUEST_2);
        CategoryResponse categoryResponse3 = categoryService.save(creator.getId(), CATEGORY_CREATE_REQUEST_3);

        MemberResponse member = memberService.save(MEMBER);
        subscriptionService.save(member.getId(), categoryResponse1.getId(), CREATE_REQUEST_RED);
        subscriptionService.save(member.getId(), categoryResponse2.getId(), CREATE_REQUEST_BLUE);
        subscriptionService.save(member.getId(), categoryResponse3.getId(), CREATE_REQUEST_YELLOW);

        // when
        SubscriptionsResponse subscriptionsResponse = subscriptionService.findByMemberId(member.getId());

        // then
        assertThat(subscriptionsResponse.getSubscriptions()).hasSize(3);
    }

    @DisplayName("?????? ????????? ????????????.")
    @Test
    void ??????_?????????_????????????() {
        // given
        MemberResponse creator = memberService.save(MemberFixtures.CREATOR);
        CategoryResponse categoryResponse1 = categoryService.save(creator.getId(), CATEGORY_CREATE_REQUEST_1);
        CategoryResponse categoryResponse2 = categoryService.save(creator.getId(), CATEGORY_CREATE_REQUEST_2);
        CategoryResponse categoryResponse3 = categoryService.save(creator.getId(), CATEGORY_CREATE_REQUEST_3);

        MemberResponse member = memberService.save(MEMBER);
        SubscriptionResponse subscriptionResponse = subscriptionService.save(member.getId(), categoryResponse1.getId(),
                CREATE_REQUEST_RED);
        subscriptionService.save(member.getId(), categoryResponse2.getId(), CREATE_REQUEST_BLUE);
        subscriptionService.save(member.getId(), categoryResponse3.getId(), CREATE_REQUEST_YELLOW);

        // when
        subscriptionService.deleteByIdAndMemberId(subscriptionResponse.getId(), member.getId());

        // then
        assertThat(subscriptionService.findByMemberId(member.getId()).getSubscriptions()).hasSize(2);
    }

    @DisplayName("????????? ?????? ????????? ?????? ????????? ????????? ?????? ????????? ?????????.")
    @Test
    void ?????????_??????_?????????_??????_?????????_?????????_??????_?????????_?????????() {
        // given
        MemberResponse member = memberService.save(MEMBER);

        // when & then
        assertThatThrownBy(() -> subscriptionService.deleteByIdAndMemberId(0L, member.getId()))
                .isInstanceOf(NoPermissionException.class);
    }
}

package com.allog.dallog.common.builder;

import static com.allog.dallog.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.categoryrole.domain.CategoryRoleType.NONE;
import static com.allog.dallog.common.Constants.외부_카테고리_ID;
import static com.allog.dallog.subscription.domain.Color.COLOR_1;

import com.allog.dallog.auth.domain.OAuthToken;
import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryType;
import com.allog.dallog.category.domain.ExternalCategoryDetail;
import com.allog.dallog.categoryrole.domain.CategoryRole;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.schedule.domain.Schedule;
import com.allog.dallog.subscription.domain.Subscription;
import java.time.LocalDateTime;

public final class GivenBuilder {

    private final BuilderSupporter bs;

    private Member member;
    private Category category;
    private CategoryRole categoryRole;
    private Subscription subscription;
    private Schedule schedule;

    public GivenBuilder(final BuilderSupporter bs) {
        this.bs = bs;
    }

    public GivenBuilder 회원_가입을_한다(final String email, final String name,
                                  final String profile) {
        Member member = new Member(email, name, profile, SocialType.GOOGLE);
        this.member = bs.memberRepository().save(member);
        OAuthToken oAuthToken = new OAuthToken(this.member, "aaa");
        bs.oAuthTokenRepository().save(oAuthToken);
        return this;
    }

    public GivenBuilder 카테고리를_생성한다(final String categoryName,
                                   final CategoryType categoryType) {
        Category category = new Category(categoryName, this.member, categoryType);
        CategoryRole categoryRole = new CategoryRole(category, this.member, ADMIN);
        Subscription subscription = new Subscription(this.member, category, COLOR_1);
        this.category = bs.categoryRepository().save(category);
        this.categoryRole = bs.categoryRoleRepository().save(categoryRole);
        this.subscription = bs.subscriptionRepository().save(subscription);
        return this;
    }

    public GivenBuilder 카테고리를_구독한다(final Category category) {
        Subscription subscription = new Subscription(this.member, category, COLOR_1);
        CategoryRole categoryRole = new CategoryRole(category, this.member, NONE);
        this.subscription = bs.subscriptionRepository().save(subscription);
        this.categoryRole = bs.categoryRoleRepository().save(categoryRole);
        return this;
    }

    public GivenBuilder 카테고리_관리_권한을_부여한다(final Member otherMember, final Category category) {
        CategoryRole categoryRole = bs.categoryRoleRepository().getByMemberIdAndCategoryId(
                otherMember.getId(),
                category.getId());
        categoryRole.changeRole(ADMIN);
        bs.categoryRoleRepository().save(categoryRole);
        return this;
    }

    public GivenBuilder 카테고리_관리_권한을_해제한다(final Member otherMember, final Category category) {
        CategoryRole categoryRole = bs.categoryRoleRepository().getByMemberIdAndCategoryId(
                otherMember.getId(),
                category.getId());
        categoryRole.changeRole(NONE);
        bs.categoryRoleRepository().save(categoryRole);
        return this;
    }

    public GivenBuilder 외부_카테고리를_등록한다(final String categoryName, final CategoryType categoryType) {
        Category category = new Category(categoryName, this.member, categoryType);
        ExternalCategoryDetail externalCategoryDetail = new ExternalCategoryDetail(category, 외부_카테고리_ID);
        Subscription subscription = new Subscription(this.member, category, COLOR_1);
        this.category = bs.categoryRepository().save(category);
        bs.externalCategoryDetailRepository().save(externalCategoryDetail);
        this.subscription = bs.subscriptionRepository().save(subscription);
        return this;
    }

    public GivenBuilder 일정을_생성한다(final String title, final LocalDateTime start, final LocalDateTime end,
                                 final String memo) {
        Schedule schedule = new Schedule(this.category, title, start, end, memo);
        this.schedule = bs.scheduleRepository().save(schedule);
        return this;
    }

    public Member 회원() {
        return member;
    }

    public Category 카테고리() {
        return category;
    }

    public Subscription 구독() {
        return subscription;
    }

    public Schedule 카테고리_일정() {
        return schedule;
    }
}

package com.allog.dallog.domain.composition.application;

import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;

import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.application.MemberAfterEvent;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import javax.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class MemberSaveAfterEvent implements MemberAfterEvent {

    private static final int ARGS_MEMBER_INDEX = 0;
    private static final String PERSONAL_CATEGORY_NAME = "내 일정";

    private final CategoryService categoryService;
    private final SubscriptionService subscriptionService;

    public MemberSaveAfterEvent(final CategoryService categoryService, final SubscriptionService subscriptionService) {
        this.categoryService = categoryService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    @Override
    public void process(final Object... args) {
        validateArgs(args);
        Member newMember = parseMember(args);

        CategoryCreateRequest request = new CategoryCreateRequest(PERSONAL_CATEGORY_NAME, PERSONAL);
        CategoryResponse newCategory = categoryService.save(newMember.getId(), request);
        subscriptionService.save(newMember.getId(), newCategory.getId());
    }

    private void validateArgs(final Object... args) {
        if (args.length != 1 || !(args[ARGS_MEMBER_INDEX] instanceof Member)) {
            throw new IllegalArgumentException("member save after event에서 필요한 매개변수 목록과 일치하지 않습니다.");
        }
    }

    private Member parseMember(final Object[] args) {
        return (Member) args[ARGS_MEMBER_INDEX];
    }
}

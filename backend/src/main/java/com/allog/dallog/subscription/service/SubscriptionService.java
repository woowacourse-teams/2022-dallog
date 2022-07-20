package com.allog.dallog.subscription.service;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.service.CategoryService;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.service.MemberService;
import com.allog.dallog.subscription.domain.Subscription;
import com.allog.dallog.subscription.dto.request.SubscriptionCreateRequest;
import com.allog.dallog.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.subscription.exception.NoSuchSubscriptionException;
import com.allog.dallog.subscription.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository, final MemberService memberService,
                               final CategoryService categoryService) {
        this.subscriptionRepository = subscriptionRepository;
        this.memberService = memberService;
        this.categoryService = categoryService;
    }

    @Transactional
    public SubscriptionResponse save(final Long memberId, final Long categoryId,
                                     final SubscriptionCreateRequest request) {
        Member member = memberService.getMember(memberId);
        Category category = categoryService.getCategory(categoryId);

        Subscription subscription = subscriptionRepository.save(new Subscription(member, category, request.getColor()));

        return new SubscriptionResponse(subscription);
    }

    public SubscriptionResponse findById(final Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NoSuchSubscriptionException::new);

        return new SubscriptionResponse(subscription);
    }
}

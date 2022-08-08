package com.allog.dallog.domain.subscription.application;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionCreateRequest;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import java.util.List;
import java.util.stream.Collectors;
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
        if (subscriptionRepository.existsByMemberIdAndCategoryId(memberId, categoryId)) {
            throw new ExistSubscriptionException();
        }

        Member member = memberService.getMember(memberId);
        Category category = categoryService.getCategory(categoryId);

        Subscription subscription = subscriptionRepository.save(new Subscription(member, category, request.getColor()));

        return new SubscriptionResponse(subscription);
    }

    public SubscriptionsResponse findByMemberId(final Long memberId) {
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(memberId);

        List<SubscriptionResponse> subscriptionResponses = subscriptions.stream()
                .map(SubscriptionResponse::new)
                .collect(Collectors.toList());

        return new SubscriptionsResponse(subscriptionResponses);
    }

    public SubscriptionResponse findById(final Long id) {
        Subscription subscription = getSubscription(id);

        return new SubscriptionResponse(subscription);
    }

    public List<Subscription> getAllByMemberId(final Long memberId) {
        return subscriptionRepository.findByMemberId(memberId);
    }

    @Transactional
    public void update(final Long id, final Long memberId, final SubscriptionUpdateRequest request) {
        if (!subscriptionRepository.existsByIdAndMemberId(id, memberId)) {
            throw new NoPermissionException();
        }

        Subscription subscription = getSubscription(id);
        subscription.change(request.getColor(), request.isChecked());
    }

    private Subscription getSubscription(final Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(NoSuchSubscriptionException::new);
    }

    @Transactional
    public void deleteByIdAndMemberId(final Long id, final Long memberId) {
        if (!subscriptionRepository.existsByIdAndMemberId(id, memberId)) {
            throw new NoPermissionException();
        }

        subscriptionRepository.deleteByIdAndMemberId(id, memberId);
    }
}

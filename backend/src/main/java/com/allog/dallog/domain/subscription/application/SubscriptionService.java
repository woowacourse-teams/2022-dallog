package com.allog.dallog.domain.subscription.application;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
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
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ColorPicker colorPicker;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository,
                               final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                               final ColorPicker colorPicker) {
        this.subscriptionRepository = subscriptionRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.colorPicker = colorPicker;
    }

    @Transactional
    public SubscriptionResponse save(final Long memberId, final Long categoryId) {
        validateAlreadyExists(memberId, categoryId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoSuchMemberException::new);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(NoSuchCategoryException::new);
        validatePermission(memberId, category);

        Color color = Color.pick(colorPicker.pickNumber());
        Subscription subscription = subscriptionRepository.save(new Subscription(member, category, color));
        return new SubscriptionResponse(subscription);
    }

    private void validateAlreadyExists(final Long memberId, final Long categoryId) {
        if (subscriptionRepository.existsByMemberIdAndCategoryId(memberId, categoryId)) {
            throw new ExistSubscriptionException();
        }
    }

    private void validatePermission(final Long memberId, final Category category) {
        if (category.isPersonal() && !category.isCreator(memberId)) {
            throw new NoPermissionException("구독 권한이 없는 카테고리입니다.");
        }
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

    public List<SubscriptionResponse> findByCategoryId(final Long categoryId) {
        return subscriptionRepository.findByCategoryId(categoryId)
                .stream()
                .map(SubscriptionResponse::new)
                .collect(Collectors.toList());
    }

    public List<Subscription> getAllByMemberId(final Long memberId) {
        return subscriptionRepository.findByMemberId(memberId);
    }

    @Transactional
    public void update(final Long id, final Long memberId, final SubscriptionUpdateRequest request) {
        validateSubscriptionPermission(id, memberId);

        Subscription subscription = getSubscription(id);
        subscription.change(request.getColor(), request.isChecked());
    }

    private Subscription getSubscription(final Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(NoSuchSubscriptionException::new);
    }

    @Transactional
    public void deleteById(final Long id, final Long memberId) {
        Subscription subscription = getSubscription(id);

        validateSubscriptionPermission(id, memberId);
        validateCategoryCreator(subscription.getCategory(), memberId);

        subscriptionRepository.deleteById(id);
    }

    private void validateSubscriptionPermission(final Long id, final Long memberId) {
        if (!subscriptionRepository.existsByIdAndMemberId(id, memberId)) {
            throw new NoPermissionException();
        }
    }

    private void validateCategoryCreator(final Category category, final Long memberId) {
        if (category.isCreator(memberId)) {
            throw new NoPermissionException("내가 만든 카테고리는 구독 취소 할 수 없습니다.");
        }
    }
}

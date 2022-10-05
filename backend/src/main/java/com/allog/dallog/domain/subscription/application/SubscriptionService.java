package com.allog.dallog.domain.subscription.application;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.domain.subscription.exception.NotAbleToUnsubscribeException;
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
    private final CategoryRoleRepository categoryRoleRepository;
    private final ColorPicker colorPicker;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository,
                               final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                               final CategoryRoleRepository categoryRoleRepository, final ColorPicker colorPicker) {
        this.subscriptionRepository = subscriptionRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.categoryRoleRepository = categoryRoleRepository;
        this.colorPicker = colorPicker;
    }

    @Transactional
    public SubscriptionResponse save(final Long memberId, final Long categoryId) {
        subscriptionRepository.validateNotExistsByMemberIdAndCategoryId(memberId, categoryId);

        Member member = memberRepository.getById(memberId);
        Category category = categoryRepository.getById(categoryId);
        category.validateSubscriptionPossible(member);

        Subscription savedSubscription = createSubscription(member, category);
        createCategoryRole(member, category);

        return new SubscriptionResponse(savedSubscription);
    }

    private Subscription createSubscription(final Member member, final Category category) {
        Color color = Color.pick(colorPicker.pickNumber());
        return subscriptionRepository.save(new Subscription(member, category, color));
    }

    private void createCategoryRole(final Member member, final Category category) {
        CategoryRole categoryRole = new CategoryRole(category, member, CategoryRoleType.NONE);
        categoryRoleRepository.save(categoryRole);
    }

    public SubscriptionResponse findById(final Long id) {
        Subscription subscription = subscriptionRepository.getById(id);
        return new SubscriptionResponse(subscription);
    }

    public SubscriptionsResponse findByMemberId(final Long memberId) {
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(memberId);

        List<SubscriptionResponse> subscriptionResponses = subscriptions.stream()
                .map(SubscriptionResponse::new)
                .collect(Collectors.toList());

        return new SubscriptionsResponse(subscriptionResponses);
    }

    public List<SubscriptionResponse> findByCategoryId(final Long categoryId) {
        return subscriptionRepository.findByCategoryId(categoryId)
                .stream()
                .map(SubscriptionResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(final Long id, final Long memberId, final SubscriptionUpdateRequest request) {
        subscriptionRepository.validateExistsByIdAndMemberId(id, memberId);
        Subscription subscription = subscriptionRepository.getById(id);
        subscription.change(request.getColor(), request.isChecked());
    }

    @Transactional
    public void delete(final Long id, final Long memberId) {
        Subscription subscription = subscriptionRepository.getById(id);
        subscription.validateDeletePossible(memberId);
        subscriptionRepository.deleteById(id);

        deleteCategoryRole(memberId, subscription);
    }

    private void deleteCategoryRole(final Long memberId, final Subscription subscription) {
        Category category = subscription.getCategory();
        CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(memberId, category.getId());

        if (!categoryRole.isNone()) {
            throw new NotAbleToUnsubscribeException("해당 카테고리에 관리자로 참여중이므로 구독을 해제할 수 없습니다.");
        }

        categoryRoleRepository.deleteById(categoryRole.getId());
    }
}

package com.allog.dallog.domain.subscription.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsByMemberIdAndCategoryId(final Long memberId, final Long categoryId);

    List<Subscription> findByMemberId(final Long memberId);

    List<Subscription> findByCategoryId(final Long categoryId);

    boolean existsByIdAndMemberId(final Long id, final Long memberId);

    void deleteByCategoryIdIn(final List<Long> categoryIds);
}

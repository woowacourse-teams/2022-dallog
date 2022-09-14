package com.allog.dallog.domain.subscription.domain;

import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsByMemberIdAndCategoryId(final Long memberId, final Long categoryId);

    List<Subscription> getByMemberId(final Long memberId);

    List<Subscription> findByCategoryId(final Long categoryId);

    boolean existsByIdAndMemberId(final Long id, final Long memberId);

    void deleteByCategoryIdIn(final List<Long> categoryIds);

    default Subscription getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchSubscriptionException::new);
    }

    default void validateExistsSubscription(final Long memberId, final Long categoryId) {
        if (this.existsByMemberIdAndCategoryId(memberId, categoryId)) {
            throw new ExistSubscriptionException();
        }
    }
}

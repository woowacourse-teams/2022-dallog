package com.allog.dallog.domain.subscription.domain;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsByMemberIdAndCategoryId(final Long memberId, final Long categoryId);

    boolean existsByIdAndMemberId(final Long id, final Long memberId);

    @EntityGraph(attributePaths = {"category", "category.member"})
    List<Subscription> findByMemberId(final Long memberId);

    @EntityGraph(attributePaths = {"category", "category.member"})
    List<Subscription> findByCategoryId(final Long categoryId);

    void deleteByMemberId(final Long id);

    void deleteByIdIn(final List<Long> categoryIds);

    void deleteByCategoryIdIn(final List<Long> id);

    default Subscription getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchSubscriptionException::new);
    }

    default void validateNotExistsByMemberIdAndCategoryId(final Long memberId, final Long categoryId) {
        if (existsByMemberIdAndCategoryId(memberId, categoryId)) {
            throw new ExistSubscriptionException();
        }
    }

    default void validateExistsByIdAndMemberId(final Long id, final Long memberId) {
        if (!existsByIdAndMemberId(id, memberId)) {
            throw new NoPermissionException();
        }
    }
}

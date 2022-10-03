package com.allog.dallog.domain.member.domain;

import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(final String email);

    boolean existsByEmail(final String email);

    @Query("SELECT m "
            + "FROM Subscription s "
            + "JOIN s.member m "
            + "WHERE s.category.id = :categoryId")
    List<Member> findSubscribers(final Long categoryId);

    default Member getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchMemberException::new);
    }

    default Member getByEmail(final String email) {
        return findByEmail(email)
                .orElseThrow(NoSuchMemberException::new);
    }

    default void validateExistsById(final Long id) {
        if (!existsById(id)) {
            throw new NoSuchMemberException();
        }
    }
}

package com.allog.dallog.domain.member.application;

import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.FIND_SUBSCRIBERS;

import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.request.MemberUpdateRequest;
import com.allog.dallog.domain.member.dto.response.MemberResponse;
import com.allog.dallog.domain.member.dto.response.SubscribersResponse;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CategoryRoleRepository categoryRoleRepository;

    public MemberService(final MemberRepository memberRepository,
                         final SubscriptionRepository subscriptionRepository,
                         final CategoryRoleRepository categoryRoleRepository) {
        this.memberRepository = memberRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.categoryRoleRepository = categoryRoleRepository;
    }

    public MemberResponse findById(final Long id) {
        return new MemberResponse(memberRepository.getById(id));
    }

    public MemberResponse findBySubscriptionId(final Long subscriptionId) {
        Subscription subscription = subscriptionRepository.getById(subscriptionId);

        Member member = subscription.getMember();
        return new MemberResponse(member);
    }

    public SubscribersResponse findSubscribers(final Long loginMemberId, final Long categoryId) {
        CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(loginMemberId, categoryId);
        categoryRole.validateAuthority(FIND_SUBSCRIBERS);

        List<CategoryRole> categoryRoles = categoryRoleRepository.findByCategoryId(categoryId);
        return new SubscribersResponse(categoryRoles);
    }

    @Transactional
    public void update(final Long id, final MemberUpdateRequest request) {
        Member member = memberRepository.getById(id);
        member.change(request.getDisplayName());
    }
}

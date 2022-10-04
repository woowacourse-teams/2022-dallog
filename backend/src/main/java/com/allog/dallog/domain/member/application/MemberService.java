package com.allog.dallog.domain.member.application;

import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.FIND_SUBSCRIBERS;

import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.request.MemberUpdateRequest;
import com.allog.dallog.domain.member.dto.response.MemberResponse;
import com.allog.dallog.domain.member.dto.response.SubscribersResponse;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ScheduleRepository scheduleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final CategoryRoleRepository categoryRoleRepository;

    public MemberService(final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                         final ScheduleRepository scheduleRepository,
                         final SubscriptionRepository subscriptionRepository,
                         final OAuthTokenRepository oAuthTokenRepository,
                         final CategoryRoleRepository categoryRoleRepository) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.scheduleRepository = scheduleRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
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
        // 구독자 목록을 조회할 때 CategoryRoleType도 함께 DTO에 포함해야하므로 CategoryRole 리스트를 가져와 DTO를 생성
    }

    @Transactional
    public void update(final Long id, final MemberUpdateRequest request) {
        Member member = memberRepository.getById(id);
        member.change(request.getDisplayName());
    }

    @Transactional
    public void deleteById(final Long id) {
        List<Long> categoryIds = categoryRepository.findByMemberId(id)
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        scheduleRepository.deleteByCategoryIdIn(categoryIds);
        subscriptionRepository.deleteByCategoryIdIn(categoryIds);
        categoryRepository.deleteByMemberId(id);
        categoryRoleRepository.deleteByMemberId(id);

        oAuthTokenRepository.deleteByMemberId(id);
        memberRepository.deleteById(id);
    }
}

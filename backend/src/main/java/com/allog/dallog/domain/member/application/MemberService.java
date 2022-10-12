package com.allog.dallog.domain.member.application;

import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.FIND_SUBSCRIBERS;

import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.domain.TokenRepository;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.request.MemberUpdateRequest;
import com.allog.dallog.domain.member.dto.response.MemberResponse;
import com.allog.dallog.domain.member.dto.response.SubscribersResponse;
import com.allog.dallog.domain.member.exception.InvalidMemberException;
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
    private final ExternalCategoryDetailRepository externalCategoryDetailRepository;
    private final ScheduleRepository scheduleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final CategoryRoleRepository categoryRoleRepository;
    private final TokenRepository tokenRepository;

    public MemberService(final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                         final ExternalCategoryDetailRepository externalCategoryDetailRepository,
                         final ScheduleRepository scheduleRepository,
                         final SubscriptionRepository subscriptionRepository,
                         final OAuthTokenRepository oAuthTokenRepository,
                         final CategoryRoleRepository categoryRoleRepository,
                         final TokenRepository tokenRepository) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.externalCategoryDetailRepository = externalCategoryDetailRepository;
        this.scheduleRepository = scheduleRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.categoryRoleRepository = categoryRoleRepository;
        this.tokenRepository = tokenRepository;
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

    @Transactional
    public void deleteById(final Long id) {
        List<CategoryRole> categoryRoles = categoryRoleRepository.findByMemberId(id)
                .stream()
                .filter(CategoryRole::isAdmin)
                .collect(Collectors.toList());

        for (CategoryRole categoryRole : categoryRoles) {
            Category category = categoryRole.getCategory();
            if (!categoryRoleRepository.isMemberSoleAdminInCategory(id, category.getId())) {
                throw new InvalidMemberException("회원의 카테고리 중 유일한 편집자 권한이 아닌 카테고리가 있습니다.");
            }
        }

        List<Long> categoryIds = categoryRoles
                .stream()
                .map(CategoryRole::getCategory)
                .map(Category::getId)
                .collect(Collectors.toList());

        //TODO 내 카테고리를 구독한 사람들의 구독 데이터와 카테고리권한 데이터를 삭제하는 로직 => 수정 필요
        for (Long categoryId : categoryIds) {
            List<Subscription> subscriptions = subscriptionRepository.findByCategoryId(categoryId);

            //TODO 내 카테고리를 구독한 사람들의 구독 데이터 삭제
            List<Long> subscriptionIds = subscriptions.stream()
                    .map(Subscription::getId)
                    .collect(Collectors.toList());

            //TODO 내 카테고리에 대한 다른 사람들의 카테고리권한 데이터 삭제
            List<Member> members = subscriptions.stream()
                    .map(Subscription::getMember)
                    .collect(Collectors.toList());

            subscriptionRepository.deleteByIdIn(subscriptionIds);
            members.forEach(member -> categoryRoleRepository.deleteByMemberId(member.getId()));
        }

        scheduleRepository.deleteByCategoryIdIn(categoryIds);

        subscriptionRepository.deleteByMemberId(id);

        externalCategoryDetailRepository.deleteByCategoryIdIn(categoryIds);

        categoryRoleRepository.deleteByCategoryIdIn(categoryIds);
        categoryRoleRepository.deleteByMemberId(id);

        List<Category> categories = categoryRepository.findByMemberId(id);
        categories.forEach(category -> category.setMember(null));

        categoryRepository.deleteByIdIn(categoryIds);
        oAuthTokenRepository.deleteByMemberId(id);
        tokenRepository.deleteByMemberId(id);
        memberRepository.deleteById(id);
    }
}

package com.allog.dallog.domain.member.application;

import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.FIND_SUBSCRIBERS;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;

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
        List<CategoryRole> adminCategoryRoles = categoryRoleRepository.findByMemberId(id)
                .stream()
                .filter(CategoryRole::isAdmin)
                .collect(Collectors.toList());

        for (CategoryRole categoryRole : adminCategoryRoles) {
            Category category = categoryRole.getCategory();
            if (category.isPersonal()) {
                continue;
            }
            if (category.isExternal()) {
                continue;
            }
            if (categoryRoleRepository.isMemberSoleAdminInCategory(id, category.getId())) {
                throw new InvalidMemberException("회원의 카테고리 중 유일한 편집자 권한이 아닌 카테고리가 있습니다.");
            }
        }

        List<Long> adminCategoryIds = adminCategoryRoles.stream()
                .map(CategoryRole::getCategory)
                .map(Category::getId)
                .collect(Collectors.toList());

        for (Long adminCategoryId : adminCategoryIds) {
            Category category = categoryRepository.getById(adminCategoryId);
            if (category.isPersonal()) {
                scheduleRepository.deleteByCategoryId(category.getId());
                subscriptionRepository.deleteByCategoryId(category.getId());
                categoryRoleRepository.deleteById(id);
                categoryRepository.deleteById(category.getId());
            }
            if (category.isExternal()) {
                subscriptionRepository.deleteById(category.getId());
                externalCategoryDetailRepository.deleteByCategoryId(category.getId());
                categoryRoleRepository.deleteByMemberIdAndCategoryId(id, category.getId());
                categoryRepository.deleteById(category.getId());
            }
            if (category.isInternal() && !category.isPersonal()) {
                // TODO: 로직 마지막 Member 삭제시 외래키 연관관계가 있는 카테고리들이 있어서 Member 삭제가 안되는 에러가 있습니다.
                // TODO: 그래서, ADMIN이 2명 이상인 경우 Category의 member_id를 변경해주기 위한 로직입니다. Category에서 member가 사라지면 삭제해야할 로직입니다.
                List<CategoryRole> categoryRoles = categoryRoleRepository.findByCategoryIdAndCategoryRoleType(
                        category.getId(), ADMIN);
                Member member = memberRepository.getById(id);
                Member otherMember = memberRepository.getById(categoryRoles.get(0).getMember().getId());
                if (!member.hasSameId(otherMember.getId())) {
                    category.setMember(otherMember);
                }
                if (member.hasSameId(otherMember.getId())) {
                    otherMember = memberRepository.getById(categoryRoles.get(1).getMember().getId());
                    category.setMember(otherMember);
                }

                subscriptionRepository.deleteByMemberIdAndCategoryId(id, category.getId());
                categoryRoleRepository.deleteByMemberIdAndCategoryId(id, category.getId());
            }
        }

        List<CategoryRole> noneCategoryRoles = categoryRoleRepository.findByMemberId(id)
                .stream()
                .filter(CategoryRole::isNone)
                .collect(Collectors.toList());

        List<Long> noneCategoryIds = noneCategoryRoles.stream()
                .map(CategoryRole::getCategory)
                .map(Category::getId)
                .collect(Collectors.toList());

        for (Long noneCategoryId : noneCategoryIds) {
            Category category = categoryRepository.getById(noneCategoryId);

            // TODO: 로직 마지막 Member 삭제시 외래키 연관관계가 있는 카테고리들이 있어서 Member 삭제가 안되는 에러가 있습니다.
            // TODO: 그래서, ADMIN이 2명 이상인 경우 Category의 member_id를 변경해주기 위한 로직입니다. Category에서 member가 사라지면 삭제해야할 로직입니다.
            List<CategoryRole> categoryRoles = categoryRoleRepository.findByCategoryIdAndCategoryRoleType(
                    category.getId(), ADMIN);
            Member member = memberRepository.getById(categoryRoles.get(0).getMember().getId());
            category.setMember(member);

            subscriptionRepository.deleteByMemberIdAndCategoryId(id, category.getId());
            categoryRoleRepository.deleteByMemberIdAndCategoryId(id, category.getId());
        }

        // TODO : 내가 만든 카테고리 일때 구독 해제를 하면, 구독과 카테고리 권한에 대한 데이터가 사라집니다.
        // TODO : 이런 경우에도 Member 삭제시 외래키 연관관계가 있는 카테고리들이 발생할수 있어서 이를 처리하기 위한 로직입니다.
        List<Category> creatorCategories = categoryRepository.findByMemberId(id)
                .stream()
                .filter(category -> category.isCreatorId(id))
                .collect(Collectors.toList());

        for (Category creatorCategory : creatorCategories) {
            List<CategoryRole> categoryRoles = categoryRoleRepository.findByCategoryIdAndCategoryRoleType(
                    creatorCategory.getId(), ADMIN);
            Member member = memberRepository.getById(categoryRoles.get(0).getMember().getId());
            creatorCategory.setMember(member);
        }

        oAuthTokenRepository.deleteByMemberId(id);
        tokenRepository.deleteByMemberId(id);
        memberRepository.deleteById(id);
    }
}

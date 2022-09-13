package com.allog.dallog.domain.composition.application;

import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;

import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.SocialType;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class RegisterService {

    private static final String PERSONAL_CATEGORY_NAME = "내 일정";

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final SubscriptionService subscriptionService;

    public RegisterService(final MemberService memberService, final CategoryService categoryService,
                           final SubscriptionService subscriptionService) {
        this.memberService = memberService;
        this.categoryService = categoryService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public MemberResponse register(final OAuthMember oAuthMember) {
        MemberResponse memberResponse = createMember(oAuthMember);
        Long categoryId = createPersonalCategory(memberResponse.getId()).getId();
        subscriptionService.save(memberResponse.getId(), categoryId);
        return memberResponse;
    }

    private MemberResponse createMember(final OAuthMember oAuthMember) {
        Member member = new Member(oAuthMember.getEmail(), oAuthMember.getDisplayName(),
                oAuthMember.getProfileImageUrl(), SocialType.GOOGLE);
        return memberService.save(member);
    }

    private CategoryResponse createPersonalCategory(final Long memberId) {
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest(PERSONAL_CATEGORY_NAME, PERSONAL);
        return categoryService.saveCategory(memberId, categoryCreateRequest);
    }

    @Transactional
    public void deleteByMemberId(final Long memberId) {
        categoryService.deleteByMemberId(memberId);
        memberService.deleteById(memberId);
    }
}

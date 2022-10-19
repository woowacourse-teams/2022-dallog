package com.allog.dallog.domain.member.application;

import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.domain.TokenRepository;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
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
		final ScheduleRepository scheduleRepository, final SubscriptionRepository subscriptionRepository,
		final OAuthTokenRepository oAuthTokenRepository, final CategoryRoleRepository categoryRoleRepository,
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
	}
}

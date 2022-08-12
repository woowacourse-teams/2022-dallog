package com.allog.dallog.domain.member.application;

import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.member.dto.MemberUpdateRequest;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final SubscriptionRepository subscriptionRepository;

    public MemberService(final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                         final OAuthTokenRepository oAuthTokenRepository,
                         final SubscriptionRepository subscriptionRepository) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public MemberResponse save(final Member member) {
        Member newMember = memberRepository.save(member);
        return new MemberResponse(newMember);
    }

    public MemberResponse findById(final Long id) {
        return new MemberResponse(getMember(id));
    }

    @Transactional
    public void update(final Long id, final MemberUpdateRequest request) {
        Member member = getMember(id);
        member.change(request.getDisplayName());
    }

    @Transactional
    public void delete(final Long id) {
        oAuthTokenRepository.deleteByMemberId(id);
        subscriptionRepository.deleteByMemberId(id);
        categoryRepository.deleteByMemberId(id);
        memberRepository.deleteById(id);
    }

    public Member getMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(NoSuchMemberException::new);
    }

    public Member getByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(NoSuchMemberException::new);
    }

    public boolean existsByEmail(final String email) {
        return memberRepository.existsByEmail(email);
    }

    public void validateExistsMember(final Long id) {
        if (!memberRepository.existsById(id)) {
            throw new NoSuchMemberException("존재하지 않는 회원입니다.");
        }
    }
}

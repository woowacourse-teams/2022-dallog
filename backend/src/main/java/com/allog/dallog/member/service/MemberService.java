package com.allog.dallog.member.service;

import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.MemberRepository;
import com.allog.dallog.member.dto.MemberResponse;
import com.allog.dallog.member.exception.NoSuchMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse save(final Member member) {
        return new MemberResponse(memberRepository.save(member));
    }

    public MemberResponse findById(final Long id) {
        return new MemberResponse(getMember(id));
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

    public boolean existsById(final Long id) {
        return memberRepository.existsById(id);
    }
}

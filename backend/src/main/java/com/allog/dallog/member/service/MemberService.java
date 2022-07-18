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
    public Member save(final Member member) {
        return memberRepository.save(member);
    }

    public MemberResponse findById(final Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NoSuchMemberException::new);

        return new MemberResponse(member);
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(NoSuchMemberException::new);
    }

    public boolean existsByEmail(final String email) {
        return memberRepository.existsByEmail(email);
    }
}

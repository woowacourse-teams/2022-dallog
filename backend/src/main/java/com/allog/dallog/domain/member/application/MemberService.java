package com.allog.dallog.domain.member.application;

import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
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

    public void validateExistsMember(final Long id) {
        if (!memberRepository.existsById(id)) {
            throw new NoSuchMemberException("존재하지 않는 회원입니다.");
        }
    }
}

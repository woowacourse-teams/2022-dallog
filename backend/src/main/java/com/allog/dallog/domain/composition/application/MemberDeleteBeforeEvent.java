package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.auth.application.OAuthTokenService;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.member.application.MemberBeforeEvent;
import com.allog.dallog.domain.member.domain.Member;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MemberDeleteBeforeEvent implements MemberBeforeEvent {

    private static final int ARGS_SIZE = 1;
    private static final int ARGS_MEMBER_INDEX = 0;

    private final OAuthTokenService oAuthTokenService;
    private final CategoryService categoryService;

    public MemberDeleteBeforeEvent(final OAuthTokenService oAuthTokenService, final CategoryService categoryService) {
        this.oAuthTokenService = oAuthTokenService;
        this.categoryService = categoryService;
    }

    @Transactional
    @Override
    public void process(final Object... args) {
        validateArgs(args);
        Member member = parseMember(args);

        oAuthTokenService.deleteByMemberId(member.getId());
        categoryService.deleteByMemberId(member.getId());
    }

    private void validateArgs(Object... args) {
        if (args.length != ARGS_SIZE || !(args[ARGS_MEMBER_INDEX] instanceof Member)) {
            throw new IllegalArgumentException("MemberDeleteAfterEvent에서 필요한 매개변수 목록과 일치하지 않습니다.");
        }
    }

    private Member parseMember(final Object... args) {
        return (Member) args[ARGS_MEMBER_INDEX];
    }
}

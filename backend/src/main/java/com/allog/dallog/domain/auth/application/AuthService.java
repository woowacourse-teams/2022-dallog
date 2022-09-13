package com.allog.dallog.domain.auth.application;

import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;

import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.TokenResponse;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.ColorPickerStrategy;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {

    private static final String PERSONAL_CATEGORY_NAME = "내 일정";
    private static final ColorPickerStrategy PICK_RANDOM_STRATEGY = () -> ThreadLocalRandom.current()
            .nextInt(Color.values().length);

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final OAuthUri oAuthUri;
    private final OAuthClient oAuthClient;
    private final TokenProvider tokenProvider;

    public AuthService(final MemberRepository memberRepository, final CategoryRepository categoryRepository,
                       final SubscriptionRepository subscriptionRepository,
                       final OAuthTokenRepository oAuthTokenRepository, final OAuthUri oAuthUri,
                       final OAuthClient oAuthClient, final TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.oAuthUri = oAuthUri;
        this.oAuthClient = oAuthClient;
        this.tokenProvider = tokenProvider;
    }

    public String generateGoogleLink(final String redirectUri) {
        return oAuthUri.generate(redirectUri);
    }

    @Transactional
    public TokenResponse generateToken(final TokenRequest tokenRequest) {
        String code = tokenRequest.getCode();
        String redirectUri = tokenRequest.getRedirectUri();

        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code, redirectUri);

        if (!memberRepository.existsByEmail(oAuthMember.getEmail())) {
            Member savedMember = memberRepository.save(parseMember(oAuthMember));
            Category savedCategory = saveCategory(savedMember);
            saveSubscription(savedMember, savedCategory);
        }
        Member foundMember = memberRepository.getByEmail(oAuthMember.getEmail());

        OAuthToken oAuthToken = getOAuthToken(oAuthMember, foundMember);
        oAuthToken.change(oAuthMember.getRefreshToken());
        String accessToken = tokenProvider.createToken(String.valueOf(foundMember.getId()));

        return new TokenResponse(accessToken);
    }

    private Category saveCategory(final Member savedMember) {
        return categoryRepository.save(new Category(PERSONAL_CATEGORY_NAME, savedMember, PERSONAL));
    }

    private Subscription saveSubscription(final Member savedMember, final Category savedCategory) {
        Color randomColor = Color.pickAny(PICK_RANDOM_STRATEGY);
        return subscriptionRepository.save(new Subscription(savedMember, savedCategory, randomColor));
    }

    private Member parseMember(final OAuthMember oAuthMember) {
        return new Member(oAuthMember.getEmail(), oAuthMember.getDisplayName(), oAuthMember.getProfileImageUrl(),
                SocialType.GOOGLE);
    }

    private OAuthToken getOAuthToken(final OAuthMember oAuthMember, final Member foundMember) {
        if (!oAuthTokenRepository.existsByMemberId(foundMember.getId())) {
            oAuthTokenRepository.save(new OAuthToken(foundMember, oAuthMember.getRefreshToken()));
        }

        return oAuthTokenRepository.getByMemberId(foundMember.getId());
    }

    public Long extractMemberId(final String accessToken) {
        tokenProvider.validateToken(accessToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(accessToken));
        if (!memberRepository.existsById(memberId)) {
            throw new NoSuchMemberException();
        }

        return memberId;
    }
}

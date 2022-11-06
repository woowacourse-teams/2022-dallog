package com.allog.dallog.domain.member.application;

import static com.allog.dallog.common.Constants.네오_이름;
import static com.allog.dallog.common.Constants.네오_이메일;
import static com.allog.dallog.common.Constants.네오_프로필_URL;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.Constants.포비_이름;
import static com.allog.dallog.common.Constants.포비_이메일;
import static com.allog.dallog.common.Constants.포비_프로필_URL;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import com.allog.dallog.domain.member.dto.request.MemberUpdateRequest;
import com.allog.dallog.domain.member.dto.response.MemberResponse;
import com.allog.dallog.domain.member.dto.response.SubscribersResponse;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends ServiceTest {

    private final MemberUpdateRequest 네오_이름_수정_요청 = new MemberUpdateRequest("모피어스");

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private User 네오;
    private User 포비;

    @BeforeEach
    void setUp() {
        네오 = new User();
        포비 = new User();
    }

    @DisplayName("id를 통해 회원을 단건 조회한다.")
    @Test
    void id를_통해_회원을_단건_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when & then
        assertThat(memberService.findById(네오.계정().getId()).getId())
                .isEqualTo(네오.계정().getId());
    }

    @DisplayName("구독 id를 기반으로 member 정보를 조회한다.")
    @Test
    void 구독_id를_기반으로_member_정보를_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when
        MemberResponse actual = memberService.findBySubscriptionId(네오.구독().getId());

        // then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(네오.계정().getId());
            assertThat(actual.getEmail()).isEqualTo(네오.계정().getEmail());
            assertThat(actual.getDisplayName()).isEqualTo(네오.계정().getDisplayName());
            assertThat(actual.getProfileImageUrl()).isEqualTo(네오.계정().getProfileImageUrl());
            assertThat(actual.getSocialType()).isEqualTo(네오.계정().getSocialType());
        });
    }

    @DisplayName("회원의 이름을 수정한다.")
    @Test
    void 회원의_이름을_수정한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL);

        // when
        memberService.update(네오.계정().getId(), 네오_이름_수정_요청);

        // then
        Member actual = memberRepository.getById(네오.계정().getId());
        assertThat(actual.getDisplayName()).isEqualTo("모피어스");
    }

    @DisplayName("특정 카테고리의 구독자 목록을 반환한다.")
    @Test
    void 특정_카테고리의_구독자_목록을_반환한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when
        SubscribersResponse actual = memberService.findSubscribers(네오.계정().getId(), 네오.카테고리().getId());

        // then
        assertThat(actual.getSubscribers().size()).isEqualTo(2);
    }

    @DisplayName("특정 카테고리의 구독자 목록을 ADMIN이 아닌 회원이 호출하면 예외가 발생한다.")
    @Test
    void 특정_카테고리의_구독자_목록을_ADMIN이_아닌_회원이_호출하면_예외가_발생한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        포비.회원_가입을_한다(포비_이메일, 포비_이름, 포비_프로필_URL)
                .카테고리를_구독한다(네오.카테고리());

        // when & then
        assertThatThrownBy(() -> memberService.findSubscribers(포비.계정().getId(), 네오.카테고리().getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    private final class User {

        private Member member;
        private Category category;
        private Subscription subscription;

        private User 회원_가입을_한다(final String email, final String name, final String profile) {
            this.member = new Member(email, name, profile, SocialType.GOOGLE);
            memberRepository.save(member);
            return this;
        }

        private User 카테고리를_등록한다(final String categoryName, final CategoryType categoryType) {
            this.category = new Category(categoryName, this.member, categoryType);
            CategoryRole categoryRole = new CategoryRole(category, this.member, ADMIN);
            this.subscription = new Subscription(this.member, category, COLOR_1);
            categoryRepository.save(category);
            categoryRoleRepository.save(categoryRole);
            subscriptionRepository.save(subscription);
            return this;
        }

        private User 카테고리를_구독한다(final Category category) {
            CategoryRole categoryRole = new CategoryRole(category, this.member, NONE);
            this.subscription = new Subscription(this.member, category, COLOR_1);
            categoryRoleRepository.save(categoryRole);
            subscriptionRepository.save(subscription);
            return this;
        }

        private Member 계정() {
            return member;
        }

        private Category 카테고리() {
            return category;
        }

        private Subscription 구독() {
            return subscription;
        }
    }
}

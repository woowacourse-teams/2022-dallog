package com.allog.dallog.domain.category.application;

import static com.allog.dallog.common.Constants.네오_이름;
import static com.allog.dallog.common.Constants.네오_이메일;
import static com.allog.dallog.common.Constants.네오_프로필_URL;
import static com.allog.dallog.common.Constants.외부_카테고리_ID;
import static com.allog.dallog.common.Constants.외부_카테고리_이름;
import static com.allog.dallog.domain.category.domain.CategoryType.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExternalCategoryDetailServiceTest extends ServiceTest {

    @Autowired
    private ExternalCategoryDetailService externalCategoryDetailService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExternalCategoryDetailRepository externalCategoryDetailRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User 네오;

    @BeforeEach
    void setUp() {
        네오 = new User();
    }

    @DisplayName("월별 일정 조회 시, 회원 ID로 해당하는 외부 연동 카테고리 전체를 조회한다.")
    @Test
    void 월별_일정_조회_시_회원_ID로_해당하는_외부_연동_카테고리의_전체를_조회한다() {
        // given
        네오.회원_가입을_한다(네오_이메일, 네오_이름, 네오_프로필_URL)
                .외부_카테고리를_등록한다(외부_카테고리_이름, GOOGLE);

        // when
        List<ExternalCategoryDetail> actual = externalCategoryDetailService.findByMemberId(네오.계정().getId());

        // then
        assertThat(actual).hasSize(1);
    }

    private final class User {

        private Member member;

        public User 회원_가입을_한다(final String email, final String name, final String profile) {
            this.member = new Member(email, name, profile, SocialType.GOOGLE);
            memberRepository.save(member);
            return this;
        }

        public User 외부_카테고리를_등록한다(final String categoryName, final CategoryType categoryType) {
            Category category = new Category(categoryName, this.member, categoryType);
            ExternalCategoryDetail externalCategoryDetail = new ExternalCategoryDetail(category, 외부_카테고리_ID);
            categoryRepository.save(category);
            externalCategoryDetailRepository.save(externalCategoryDetail);
            return this;
        }

        public Member 계정() {
            return member;
        }
    }
}

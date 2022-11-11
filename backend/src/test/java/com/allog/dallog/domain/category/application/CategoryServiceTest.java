package com.allog.dallog.domain.category.application;

import static com.allog.dallog.common.Constants.개인_카테고리_이름;
import static com.allog.dallog.common.Constants.나인_이름;
import static com.allog.dallog.common.Constants.나인_이메일;
import static com.allog.dallog.common.Constants.나인_프로필_URL;
import static com.allog.dallog.common.Constants.스터디_카테고리_이름;
import static com.allog.dallog.common.Constants.외부_카테고리_ID;
import static com.allog.dallog.common.Constants.외부_카테고리_이름;
import static com.allog.dallog.common.Constants.취업_일정_메모;
import static com.allog.dallog.common.Constants.취업_일정_시작일;
import static com.allog.dallog.common.Constants.취업_일정_제목;
import static com.allog.dallog.common.Constants.취업_일정_종료일;
import static com.allog.dallog.common.Constants.취업_카테고리_이름;
import static com.allog.dallog.common.Constants.티거_이름;
import static com.allog.dallog.common.Constants.티거_이메일;
import static com.allog.dallog.common.Constants.티거_프로필_URL;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.domain.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;
import static com.allog.dallog.domain.subscription.domain.Color.COLOR_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.event.MemberSavedEvent;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.request.ExternalCategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryDetailResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.ExistExternalCategoryException;
import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.exception.ManagingCategoryLimitExcessException;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.domain.SocialType;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class CategoryServiceTest extends ServiceTest {

    private final CategoryCreateRequest 취업_카테고리_생성_요청 = new CategoryCreateRequest(취업_카테고리_이름, NORMAL);
    private final CategoryCreateRequest 개인_카테고리_생성_요청 = new CategoryCreateRequest(개인_카테고리_이름, PERSONAL);
    private final ExternalCategoryCreateRequest 외부_카테고리_생성_요청 = new ExternalCategoryCreateRequest(외부_카테고리_ID,
            외부_카테고리_이름);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryRoleRepository categoryRoleRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private IntegrationLogicBuilder 나인;
    private IntegrationLogicBuilder 티거;

    @BeforeEach
    void setUp() {
        나인 = new IntegrationLogicBuilder();
        티거 = new IntegrationLogicBuilder();
    }

    @Test
    void 카테고리를_등록한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        // when
        CategoryResponse actual = categoryService.save(나인.회원().getId(), 취업_카테고리_생성_요청);

        // then
        assertThat(actual.getName()).isEqualTo(취업_카테고리_이름);
    }

    @Test
    void 개인_카테고리를_등록한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        // when
        CategoryResponse 개인_카테고리_응답 = categoryService.save(나인.회원().getId(), 개인_카테고리_생성_요청);

        // then
        Category actual = categoryRepository.findById(개인_카테고리_응답.getId()).get();

        assertAll(() -> {
            assertThat(actual.getName()).isEqualTo(개인_카테고리_이름);
            assertThat(actual.isPersonal()).isTrue();
        });
    }

    @Test
    void 카테고리를_등록할_때_자동으로_구독한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        // when
        categoryService.save(나인.회원().getId(), 취업_카테고리_생성_요청);

        // then
        List<Subscription> actual = subscriptionRepository.findByMemberId(나인.회원().getId());
        assertThat(actual).hasSize(1);
    }

    @Transactional
    @Test
    void 카테고리를_등록할_때_권한을_최고_관리자로_등록한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        // when
        CategoryResponse 응답 = categoryService.save(나인.회원().getId(), 취업_카테고리_생성_요청);

        // then
        CategoryRole actual = categoryRoleRepository.getByMemberIdAndCategoryId(나인.회원().getId(), 응답.getId());
        assertThat(actual.getCategoryRoleType()).isEqualTo(ADMIN);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "일이삼사오육칠팔구십일이삼사오육칠팔구십일", "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 카테고리"})
    void 카테고리를_등록할_때_이름이_공백이거나_길이가_20을_초과하면_예외를_발생한다(final String invalidName) {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        CategoryCreateRequest 카테고리_생성_요청 = new CategoryCreateRequest(invalidName, NORMAL);

        // when & then
        assertThatThrownBy(() -> categoryService.save(나인.회원().getId(), 카테고리_생성_요청))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @Test
    void 외부_카테고리를_등록한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        // when
        CategoryResponse 응답 = categoryService.save(나인.회원().getId(), 외부_카테고리_생성_요청);

        // then
        Category actual = categoryRepository.findById(응답.getId()).get();
        assertAll(() -> {
            assertThat(actual.getName()).isEqualTo(외부_카테고리_이름);
            assertThat(actual.getCategoryType()).isEqualTo(GOOGLE);
        });
    }

    @Test
    void 중복되는_외부_카테고리를_등록하면_예외를_발생한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        categoryService.save(나인.회원().getId(), 외부_카테고리_생성_요청);

        // when & then
        assertThatThrownBy(() -> categoryService.save(나인.회원().getId(), 외부_카테고리_생성_요청))
                .isInstanceOf(ExistExternalCategoryException.class);
    }

    @Test
    void 외부_카테고리를_등록할_때_자동으로_구독한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        // when
        categoryService.save(나인.회원().getId(), 외부_카테고리_생성_요청);

        // then
        List<Subscription> actual = subscriptionRepository.findByMemberId(나인.회원().getId());
        assertThat(actual).hasSize(1);
    }

    @Test
    void 저장된_회원의_개인_카테고리를_생성하고_자동으로_구독하고_카테고리_역할을_부여한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .회원();

        MemberSavedEvent event = new MemberSavedEvent(나인.회원().getId());

        // when
        categoryService.savePersonalCategory(event);

        // then
        List<Category> categories = categoryRepository.findByMemberId(나인.회원().getId());
        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(나인.회원().getId());
        List<CategoryRole> categoryRoles = categoryRoleRepository.findByMemberId(나인.회원().getId());

        assertAll(() -> {
            assertThat(categories).hasSize(1)
                    .extracting("categoryType")
                    .containsExactly(CategoryType.PERSONAL);
            assertThat(subscriptions).hasSize(1)
                    .extracting("checked")
                    .containsExactly(true);
            assertThat(categoryRoles).hasSize(1)
                    .extracting("categoryRoleType")
                    .containsExactly(ADMIN);
        });
    }

    @Test
    void 검색어가_제목에_있는_카테고리를_가져온다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(외부_카테고리_이름, GOOGLE)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리를_등록한다(스터디_카테고리_이름, NORMAL);

        // when
        CategoriesResponse actual = categoryService.findNormalByName("취업");

        // then
        assertThat(actual.getCategories()).hasSize(1);
    }

    @Test
    void 검색어가_제목에_있는_카테고리를_가져올때_개인_카테고리는_제외한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(개인_카테고리_이름, PERSONAL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리를_등록한다(스터디_카테고리_이름, NORMAL);

        // when
        CategoriesResponse actual = categoryService.findNormalByName("");

        // then
        assertThat(actual.getCategories()).hasSize(2);
    }

    @Transactional
    @Test
    void 관리권한이_최고_관리자인_카테고리_목록을_조회한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(스터디_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_등록한다(스터디_카테고리_이름, NORMAL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .카테고리를_구독한다(나인.카테고리());

        나인.내_카테고리_관리_권한을_부여한다(티거.회원());

        // when
        CategoriesResponse actual = categoryService.findScheduleEditableCategories(티거.회원().getId());

        // then
        assertThat(actual.getCategories().size()).isEqualTo(3);
    }

    @Test
    void id로_카테고리_단건_조회한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when
        CategoryDetailResponse actual = categoryService.findDetailCategoryById(나인.카테고리().getId());

        // then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(나인.카테고리().getId());
            assertThat(actual.getName()).isEqualTo(나인.카테고리().getName());
        });
    }

    @Test
    void id로_카테고리_단건_조회할_때_없으면_예외를_발생한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when & then
        assertThatThrownBy(() -> categoryService.findDetailCategoryById(나인.카테고리().getId() + 1))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @Test
    void 권한이_최고_관리자인_카테고리를_수정한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        CategoryUpdateRequest 카테고리_수정_요청 = new CategoryUpdateRequest("새로운 취업 카테고리 이름");

        // when
        categoryService.update(나인.회원().getId(), 나인.카테고리().getId(), 카테고리_수정_요청);

        //then
        Category actual = categoryRepository.getById(나인.카테고리().getId());
        assertThat(actual.getName()).isEqualTo("새로운 취업 카테고리 이름");
    }

    @Test
    void 권한이_최고_관리자가_아닌_카테고리를_수정하면_예외를_발생한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        CategoryUpdateRequest 카테고리_수정_요청 = new CategoryUpdateRequest("새로운 취업 카테고리 이름");

        // when & then
        assertThatThrownBy(() -> categoryService.update(티거.회원().getId(), 나인.카테고리().getId(), 카테고리_수정_요청))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Test
    void 카테고리를_수정할_때_카테고리가_없으면_예외를_발생한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        CategoryUpdateRequest 카테고리_수정_요청 = new CategoryUpdateRequest("새로운 취업 카테고리 이름");

        // when & then
        assertThatThrownBy(() -> categoryService.update(나인.회원().getId(), -1L, 카테고리_수정_요청))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @Test
    void 권한이_최고_관리자인_카테고리를_삭제한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        // when
        categoryService.delete(나인.회원().getId(), 나인.카테고리().getId());

        //then
        assertThatThrownBy(() -> categoryRepository.getById(나인.카테고리().getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @Test
    void 권한이_최고_관리자가_아닌_카테고리를_삭제하려_하면_예외를_발생한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        // when & then
        assertThatThrownBy(() -> categoryService.delete(티거.회원().getId(), 나인.카테고리().getId()))
                .isInstanceOf(NoCategoryAuthorityException.class);
    }

    @Test
    void 카테고리를_등록할_때_최고_관리자인_카테고리가_50개면_예외를_발생한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);
        for (int i = 0; i < 50; i++) {
            나인.카테고리를_등록한다("카테고리 " + i, NORMAL);
        }

        // when & then
        assertThatThrownBy(() -> categoryService.save(나인.회원().getId(), BE_일정_생성_요청))
                .isInstanceOf(ManagingCategoryLimitExcessException.class);
    }

    @Test
    void 없는_카테고리를_삭제하려_하면_예외를_발생한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL);

        // when & then
        assertThatThrownBy(() -> categoryService.delete(나인.회원().getId(), -1L))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @Test
    void 카테고리를_삭제할_때_등록한_일정도_모두_삭제한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL)
                .일정을_등록한다(취업_일정_제목, 취업_일정_시작일, 취업_일정_종료일, 취업_일정_메모);

        // when
        categoryService.delete(나인.회원().getId(), 나인.카테고리().getId());

        // then
        assertAll(() -> {
            assertThatThrownBy(() -> scheduleRepository.getById(나인.카테고리_일정().getId()))
                    .isInstanceOf(NoSuchScheduleException.class);
        });
    }

    @Test
    void 카테고리를_삭제할_때_구독_정보도_모두_삭제한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        티거.회원_가입을_한다(나인_이메일, 나인_이름, 나인_프로필_URL)
                .카테고리를_구독한다(나인.카테고리());

        // when
        categoryService.delete(나인.회원().getId(), 나인.카테고리().getId());

        // then
        assertThatThrownBy(() -> subscriptionRepository.getById(티거.구독().getId()))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @Transactional
    @Test
    void 카테고리를_삭제할_때_카테고리_권한도_모두_삭제한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(취업_카테고리_이름, NORMAL);

        CategoryRole 권한 = categoryRoleRepository.getByMemberIdAndCategoryId(나인.회원().getId(),
                나인.카테고리().getId());

        // when
        categoryService.delete(나인.회원().getId(), 나인.카테고리().getId());

        // then
        boolean actual = categoryRoleRepository.findById(권한.getId()).isPresent();
        assertThat(actual).isFalse();
    }

    @Test
    void 개인_카테고리는_삭제할_수_없다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(개인_카테고리_이름, PERSONAL);

        // when & then
        assertThatThrownBy(() -> categoryService.delete(나인.회원().getId(), 나인.카테고리().getId()))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @Test
    void 외부_서비스_연동_카테고리를_삭제한다() {
        // given
        나인.회원_가입을_한다(티거_이메일, 티거_이름, 티거_프로필_URL)
                .카테고리를_등록한다(외부_카테고리_이름, GOOGLE);

        // when
        categoryService.delete(나인.회원().getId(), 나인.카테고리().getId());

        // then
        assertThatThrownBy(() -> categoryService.findDetailCategoryById(나인.카테고리().getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    private final class IntegrationLogicBuilder {

        private Member member;
        private Category category;
        private CategoryRole categoryRole;
        private Subscription subscription;
        private Schedule schedule;

        private IntegrationLogicBuilder 회원_가입을_한다(final String email, final String name, final String profile) {
            Member member = new Member(email, name, profile, SocialType.GOOGLE);
            this.member = memberRepository.save(member);
            return this;
        }

        private IntegrationLogicBuilder 카테고리를_등록한다(final String categoryName, final CategoryType categoryType) {
            Category category = new Category(categoryName, this.member, categoryType);
            CategoryRole categoryRole = new CategoryRole(category, this.member, ADMIN);
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            this.category = categoryRepository.save(category);
            this.categoryRole = categoryRoleRepository.save(categoryRole);
            this.subscription = subscriptionRepository.save(subscription);
            return this;
        }

        private IntegrationLogicBuilder 카테고리를_구독한다(final Category category) {
            Subscription subscription = new Subscription(this.member, category, COLOR_1);
            CategoryRole categoryRole = new CategoryRole(category, this.member, NONE);
            this.subscription = subscriptionRepository.save(subscription);
            this.categoryRole = categoryRoleRepository.save(categoryRole);
            return this;
        }

        private IntegrationLogicBuilder 내_카테고리_관리_권한을_부여한다(final Member otherMember) {
            CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(otherMember.getId(),
                    category.getId());
            categoryRole.changeRole(ADMIN);
            categoryRoleRepository.save(categoryRole);
            return this;
        }

        private IntegrationLogicBuilder 일정을_등록한다(final String title, final LocalDateTime start, final LocalDateTime end,
                                                 final String memo) {
            Schedule schedule = new Schedule(this.category, title, start, end, memo);
            this.schedule = scheduleRepository.save(schedule);
            return this;
        }

        private Member 회원() {
            return member;
        }

        private Category 카테고리() {
            return category;
        }

        private Subscription 구독() {
            return subscription;
        }

        private Schedule 카테고리_일정() {
            return schedule;
        }
    }
}

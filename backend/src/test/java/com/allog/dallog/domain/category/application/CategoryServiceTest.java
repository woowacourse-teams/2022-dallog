package com.allog.dallog.domain.category.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.리버_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.AuthFixtures.후디_인증_코드_토큰_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.우아한테크코스_외부_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디_생성_요청;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.대한민국_공휴일_생성_요청;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.대한민국_공휴일_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회식_생성_요청;
import static com.allog.dallog.domain.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.fixtures.CategoryFixtures;
import com.allog.dallog.domain.auth.application.AuthService;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.DuplicatedExternalCategoryException;
import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class CategoryServiceTest extends ServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("새로운 카테고리를 생성한다.")
    @Test
    void 새로운_카테고리를_생성한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());

        // when
        CategoryResponse response = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // then
        assertThat(response.getName()).isEqualTo(공통_일정_이름);
    }

    @DisplayName("새로운 개인 카테고리를 생성한다.")
    @Test
    void 새로운_개인_카테고리를_생성한다() {
        // given
        Member 후디 = memberRepository.save(후디());

        // when
        CategoryResponse 내_일정_응답 = categoryService.save(후디.getId(), 내_일정_생성_요청);
        Category 내_일정 = categoryRepository.findById(내_일정_응답.getId()).get();

        // then
        assertAll(() -> {
            assertThat(내_일정.getName()).isEqualTo(CategoryFixtures.내_일정_이름);
            assertThat(내_일정.isPersonal()).isTrue();
        });
    }

    @DisplayName("새로운 카테고리를 생성 할 떄 이름이 공백이거나 길이가 20을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "일이삼사오육칠팔구십일이삼사오육칠팔구십일", "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 카테고리"})
    void 새로운_카테고리를_생성_할_때_이름이_공백이거나_길이가_20을_초과하는_경우_예외를_던진다(final String name) {
        // given
        CategoryCreateRequest request = new CategoryCreateRequest(name, NORMAL);
        Member 관리자 = memberRepository.save(관리자());

        // when & then
        assertThatThrownBy(() -> categoryService.save(관리자.getId(), request))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("새로운 외부 카테고리를 생성한다.")
    @Test
    void 새로운_외부_카테고리를_생성한다() {
        // given
        Member 후디 = memberRepository.save(후디());

        // when
        CategoryResponse 후디_대한민국_공휴일_카테고리_응답 = categoryService.save(후디.getId(), 대한민국_공휴일_생성_요청);
        Category 후디_대한민국_공휴일_카테고리 = categoryRepository.findById(후디_대한민국_공휴일_카테고리_응답.getId()).get();

        // then
        assertAll(() -> {
            assertThat(후디_대한민국_공휴일_카테고리.getName()).isEqualTo(대한민국_공휴일_이름);
            assertThat(후디_대한민국_공휴일_카테고리.getCategoryType()).isEqualTo(GOOGLE);
        });
    }

    @DisplayName("중복된 외부 카테고리를 저장하는 경우 예외를 던진다.")
    @Test
    void 중복된_외부_카테고리를_저장하는_경우_예외를_던진다() {
        // given
        Member 후디 = memberRepository.save(후디());

        // when
        categoryService.save(후디.getId(), 대한민국_공휴일_생성_요청);

        // then
        assertThatThrownBy(() -> categoryService.save(후디.getId(), 대한민국_공휴일_생성_요청))
                .isInstanceOf(DuplicatedExternalCategoryException.class);
    }

    @DisplayName("페이지와 제목을 받아 해당하는 구간의 카테고리를 가져온다.")
    @Test
    void 페이지와_제목을_받아_해당하는_구간의_카테고리를_가져온다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Long 관리자_ID = 관리자.getId();
        categoryService.save(관리자_ID, 공통_일정_생성_요청);
        categoryService.save(관리자_ID, BE_일정_생성_요청);
        categoryService.save(관리자_ID, FE_일정_생성_요청);
        categoryService.save(관리자_ID, 매트_아고라_생성_요청);
        categoryService.save(관리자_ID, 후디_JPA_스터디_생성_요청);

        PageRequest request = PageRequest.of(0, 3);

        // when
        CategoriesResponse response = categoryService.findNormalByName("일", request);

        // then
        assertThat(response.getCategories())
                .hasSize(3)
                .extracting(CategoryResponse::getName)
                .contains(공통_일정_이름, BE_일정_이름, FE_일정_이름);
    }

    @DisplayName("개인 카테고리는 전체 조회 대상에서 제외된다.")
    @Test
    void 개인_카테고리는_전체_조회_대상에서_제외된다() {
        // given
        authService.generateToken(후디_인증_코드_토큰_요청());
        authService.generateToken(리버_인증_코드_토큰_요청());

        // when
        CategoriesResponse response = categoryService.findNormalByName("", PageRequest.of(0, 10));

        // then
        assertThat(response.getCategories()).hasSize(0);
    }

    @DisplayName("회원 id와 페이지를 기반으로 카테고리를 가져온다.")
    @Test
    void 회원_id와_페이지를_기반으로_카테고리를_가져온다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Long 관리자_ID = 관리자.getId();
        categoryService.save(관리자_ID, 공통_일정_생성_요청);
        categoryService.save(관리자_ID, BE_일정_생성_요청);
        categoryService.save(관리자_ID, FE_일정_생성_요청);
        categoryService.save(관리자_ID, 매트_아고라_생성_요청);
        categoryService.save(관리자_ID, 후디_JPA_스터디_생성_요청);

        PageRequest request = PageRequest.of(1, 2);

        // when
        CategoriesResponse response = categoryService.findMineByName(관리자_ID, "", request);

        // then
        assertThat(response.getCategories())
                .hasSize(2)
                .extracting(CategoryResponse::getName)
                .contains(FE_일정_이름, 매트_아고라_이름);
    }

    @DisplayName("회원 id와 제목과 페이지를 받아 해당하는 구간의 카테고리를 가져온다.")
    @Test
    void 회원_id와_제목과_페이지를_받아_해당하는_구간의_카테고리를_가져온다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Long 관리자_ID = 관리자.getId();
        categoryService.save(관리자_ID, 공통_일정_생성_요청);
        categoryService.save(관리자_ID, BE_일정_생성_요청);
        categoryService.save(관리자_ID, FE_일정_생성_요청);
        categoryService.save(관리자_ID, 매트_아고라_생성_요청);
        categoryService.save(관리자_ID, 후디_JPA_스터디_생성_요청);

        PageRequest request = PageRequest.of(0, 3);

        // when
        CategoriesResponse response = categoryService.findMineByName(관리자_ID, "일", request);

        // then
        assertThat(response.getCategories())
                .hasSize(3)
                .extracting(CategoryResponse::getName)
                .contains(공통_일정_이름, BE_일정_이름, FE_일정_이름);
    }

    @DisplayName("id를 통해 카테고리를 단건 조회한다.")
    @Test
    void id를_통해_카테고리를_단건_조회한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when & then
        CategoryResponse 조회한_공통_일정 = categoryService.findById(공통_일정.getId());

        assertAll(() -> {
            assertThat(조회한_공통_일정.getId()).isEqualTo(공통_일정.getId());
            assertThat(조회한_공통_일정.getName()).isEqualTo(공통_일정.getName());
        });
    }

    @DisplayName("id를 통해 카테고리를 단건 조회할 때 카테고리가 존재하지 않다면 예외가 발생한다.")
    @Test
    void id를_통해_카테고리를_단건_조회할_때_카테고리가_존재하지_않다면_예외가_발생한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when & then
        assertThatThrownBy(() -> categoryService.findById(공통_일정.getId() + 1))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("회원과 카테고리 id를 통해 카테고리를 수정한다.")
    @Test
    void 회원과_카테고리_id를_통해_카테고리를_수정한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        String 우테코_공통_일정_이름 = "우테코 공통 일정";
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(우테코_공통_일정_이름);

        // when
        categoryService.update(관리자.getId(), 공통_일정.getId(), categoryUpdateRequest);
        Category category = categoryService.getCategory(공통_일정.getId());

        //then
        assertThat(category.getName()).isEqualTo(우테코_공통_일정_이름);
    }

    @DisplayName("존재하지 않는 카테고리를 수정할 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_카테고리를_수정할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(BE_일정_이름);

        // when & then
        assertThatThrownBy(() -> categoryService.update(관리자.getId(), 공통_일정.getId() + 1, categoryUpdateRequest))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("자신이 만들지 않은 카테고리를 수정할 경우 예외를 던진다.")
    @Test
    void 자신이_만들지_않은_카테고리를_수정할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 매트 = memberRepository.save(매트());

        CategoryResponse savedCategory = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest("우테코 공통 일정");

        // when & then
        assertThatThrownBy(() -> categoryService.update(매트.getId(), savedCategory.getId(), categoryUpdateRequest))
                .isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("회원과 카테고리 id를 통해 카테고리를 삭제한다.")
    @Test
    void 회원과_카테고리_id를_통해_카테고리를_삭제한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when
        categoryService.deleteById(관리자.getId(), 공통_일정.getId());

        //then
        assertThatThrownBy(() -> categoryService.getCategory(공통_일정.getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("존재하지 않는 카테고리를 삭제할 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_카테고리를_삭제할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when & then
        assertThatThrownBy(() -> categoryService.deleteById(관리자.getId(), 공통_일정.getId() + 1))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("자신이 만들지 않은 카테고리를 삭제할 경우 예외를 던진다.")
    @Test
    void 자신이_만들지_않은_카테고리를_삭제할_경우_예외를_던진다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        Member 매트 = memberRepository.save(매트());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when & then
        assertThatThrownBy(
                () -> categoryService.deleteById(매트.getId(), 공통_일정.getId()))
                .isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("카테고리 삭제 시 연관된 일정 엔티티도 모두 제거된다.")
    @Test
    void 카테고리_삭제_시_연관된_일정_엔티티도_모두_제거된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        ScheduleResponse 알록달록_회식 = scheduleService.save(관리자.getId(), 공통_일정.getId(), 알록달록_회식_생성_요청);
        ScheduleResponse 레벨_인터뷰 = scheduleService.save(관리자.getId(), 공통_일정.getId(), 레벨_인터뷰_생성_요청);

        // when
        categoryService.deleteById(관리자.getId(), 공통_일정.getId());

        // then
        assertAll(() -> {
            assertThatThrownBy(() -> scheduleService.findById(알록달록_회식.getId()))
                    .isInstanceOf(NoSuchScheduleException.class);
            assertThatThrownBy(() -> scheduleService.findById(레벨_인터뷰.getId()))
                    .isInstanceOf(NoSuchScheduleException.class);
        });
    }

    @DisplayName("카테고리 삭제 시 연관된 구독 엔티티도 모두 제거된다.")
    @Test
    void 카테고리_삭제_시_연관된_구독_엔티티도_모두_제거된다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        Member 후디 = memberRepository.save(후디());
        SubscriptionResponse 구독 = subscriptionService.save(후디.getId(), 공통_일정.getId());

        // when
        categoryService.deleteById(관리자.getId(), 공통_일정.getId());

        // then
        assertThatThrownBy(() -> subscriptionService.findById(구독.getId()))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @DisplayName("개인 카테고리는 삭제할 수 없다.")
    @Test
    void 개인_카테고리는_삭제할_수_없다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 내_일정 = categoryService.save(관리자.getId(), 내_일정_생성_요청);
        subscriptionService.save(관리자.getId(), 내_일정.getId());

        // when & then
        assertThatThrownBy(() -> categoryService.deleteById(관리자.getId(), 내_일정.getId()))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("특정 회원의 카테고리를 전부 삭제한다.")
    @Test
    void 특정_회원의_카테고리를_전부_삭제한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);

        // when
        categoryService.deleteByMemberId(관리자.getId());

        //then
        assertThatThrownBy(() -> categoryService.getCategory(공통_일정.getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }

    @DisplayName("특정 회원의 카테고리를 삭제할 때 연관된 일정도 삭제한다.")
    @Test
    void 특정_회원의_카테고리를_삭제할_때_연관된_일정도_삭제한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        ScheduleResponse 알록달록_회식 = scheduleService.save(관리자.getId(), 공통_일정.getId(), 알록달록_회식_생성_요청);
        ScheduleResponse 레벨_인터뷰 = scheduleService.save(관리자.getId(), 공통_일정.getId(), 레벨_인터뷰_생성_요청);

        // when
        categoryService.deleteByMemberId(관리자.getId());

        // then
        assertAll(() -> {
            assertThatThrownBy(() -> scheduleService.findById(알록달록_회식.getId()))
                    .isInstanceOf(NoSuchScheduleException.class);
            assertThatThrownBy(() -> scheduleService.findById(레벨_인터뷰.getId()))
                    .isInstanceOf(NoSuchScheduleException.class);
        });
    }

    @DisplayName("외부 캘린더의 카테고리를 삭제한다.")
    @Test
    void 외부_캘린더의_카테고리를_삭제한다() {
        // given
        Member 관리자 = memberRepository.save(관리자());
        CategoryResponse 우아한테크코스_외부_일정 = categoryService.save(관리자.getId(), 우아한테크코스_외부_일정_생성_요청);

        // when
        categoryService.deleteById(관리자.getId(), 우아한테크코스_외부_일정.getId());

        // then
        assertThatThrownBy(() -> categoryService.findById(우아한테크코스_외부_일정.getId()))
                .isInstanceOf(NoSuchCategoryException.class);
    }
}

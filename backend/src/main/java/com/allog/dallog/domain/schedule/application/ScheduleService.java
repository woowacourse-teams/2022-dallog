package com.allog.dallog.domain.schedule.application;

import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.ADD_SCHEDULE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.DELETE_CATEGORY;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.UPDATE_SCHEDULE;

import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.dto.MaterialToFindSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.domain.Subscriptions;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryRoleRepository categoryRoleRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final ExternalCategoryDetailRepository externalCategoryDetailRepository;

    public ScheduleService(final ScheduleRepository scheduleRepository, final CategoryRepository categoryRepository,
                           final CategoryRoleRepository categoryRoleRepository, final MemberRepository memberRepository,
                           final SubscriptionRepository subscriptionRepository,
                           final OAuthTokenRepository oAuthTokenRepository,
                           final ExternalCategoryDetailRepository externalCategoryDetailRepository) {
        this.scheduleRepository = scheduleRepository;
        this.categoryRepository = categoryRepository;
        this.categoryRoleRepository = categoryRoleRepository;
        this.memberRepository = memberRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.externalCategoryDetailRepository = externalCategoryDetailRepository;
    }

    @Transactional
    public ScheduleResponse save(final Long memberId, final Long categoryId, final ScheduleCreateRequest request) {
        Category category = categoryRepository.getById(categoryId);
        category.validateExternalCategory();

        CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(memberId, categoryId);
        categoryRole.validateAuthority(ADD_SCHEDULE);

        Schedule schedule = scheduleRepository.save(request.toEntity(category));
        return new ScheduleResponse(schedule);
    }

    public ScheduleResponse findById(final Long id) {
        Schedule schedule = scheduleRepository.getById(id);
        return new ScheduleResponse(schedule);
    }

    public MaterialToFindSchedules findInternalByMemberIdAndDateRange(final Long memberId,
                                                                      final DateRangeRequest request) {
        Subscriptions subscriptions = new Subscriptions(subscriptionRepository.findByMemberId(memberId));
        List<IntegrationSchedule> schedules = toIntegrationSchedules(subscriptions, request);

        String refreshToken = toRefreshToken(memberId);
        List<ExternalCategoryDetail> externalCategoryDetails = toCategoryDetails(subscriptions);

        return new MaterialToFindSchedules(subscriptions, schedules, refreshToken, externalCategoryDetails);
    }

    private String toRefreshToken(final Long memberId) {
        OAuthToken oAuthToken = oAuthTokenRepository.getByMemberId(memberId);
        return oAuthToken.getRefreshToken();
    }

    private List<ExternalCategoryDetail> toCategoryDetails(final Subscriptions subscriptions) {
        return externalCategoryDetailRepository.findByCategoryIn(subscriptions.findExternalCategory());
    }

    private List<IntegrationSchedule> toIntegrationSchedules(final Subscriptions subscriptions,
                                                             final DateRangeRequest request) {
        List<Category> categories = subscriptions.findInternalCategory();
        return scheduleRepository.getByCategoriesAndBetween(categories, request.getStartDateTime(),
                request.getEndDateTime());
    }

    @Transactional
    public void update(final Long id, final Long memberId, final ScheduleUpdateRequest request) {
        Long categoryId = request.getCategoryId();
        Category categoryForUpdate = categoryRepository.getById(categoryId);
        Schedule schedule = scheduleRepository.getById(id);

        CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(memberId, categoryId);
        categoryRole.validateAuthority(UPDATE_SCHEDULE);

        schedule.change(categoryForUpdate, request.getTitle(), request.getStartDateTime(), request.getEndDateTime(),
                request.getMemo());
    }

    @Transactional
    public void delete(final Long id, final Long memberId) {
        Schedule schedule = scheduleRepository.getById(id);
        Long categoryId = schedule.getCategory().getId();

        CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(memberId, categoryId);
        categoryRole.validateAuthority(DELETE_CATEGORY);

        scheduleRepository.deleteById(id);
    }
}

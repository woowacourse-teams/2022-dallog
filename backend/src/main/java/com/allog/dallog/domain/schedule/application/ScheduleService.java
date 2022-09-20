package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.dto.AvailablePeriodMaterial;
import com.allog.dallog.domain.schedule.dto.ExternalRequestMaterial;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.domain.Subscriptions;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final ExternalCategoryDetailRepository externalCategoryDetailRepository;

    public ScheduleService(final ScheduleRepository scheduleRepository, final CategoryRepository categoryRepository,
                           final MemberRepository memberRepository, final SubscriptionRepository subscriptionRepository,
                           final OAuthTokenRepository oAuthTokenRepository,
                           final ExternalCategoryDetailRepository externalCategoryDetailRepository) {
        this.scheduleRepository = scheduleRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.externalCategoryDetailRepository = externalCategoryDetailRepository;
    }

    @Transactional
    public ScheduleResponse save(final Long memberId, final Long categoryId, final ScheduleCreateRequest request) {
        Category category = categoryRepository.getById(categoryId);
        Member member = memberRepository.getById(memberId);
        category.validateCanAddSchedule(member);
        Schedule schedule = scheduleRepository.save(request.toEntity(category));
        return new ScheduleResponse(schedule);
    }

    public ScheduleResponse findById(final Long id) {
        Schedule schedule = scheduleRepository.getById(id);
        return new ScheduleResponse(schedule);
    }

    public List<IntegrationSchedule> findInternalByMemberIdAndDateRange(final Long memberId,
                                                                        final DateRangeRequest dateRange) {
        Subscriptions subscriptions = new Subscriptions(subscriptionRepository.findByMemberId(memberId));
        return toIntegrationSchedules(dateRange, subscriptions);
    }

    private List<IntegrationSchedule> toIntegrationSchedules(final DateRangeRequest dateRange,
                                                             final Subscriptions subscriptions) {
        List<Category> categories = subscriptions.findInternalCategory();
        return scheduleRepository.getByCategoriesAndBetween(categories, dateRange.getStartDateTime(),
                dateRange.getEndDateTime());
    }

    @Transactional
    public void update(final Long id, final Long memberId, final ScheduleUpdateRequest request) {
        Long categoryId = request.getCategoryId();
        categoryRepository.validateExistsByIdAndMemberId(categoryId, memberId);

        Category categoryForUpdate = categoryRepository.getById(categoryId);
        Schedule schedule = scheduleRepository.getById(id);
        schedule.validateEditPossible(memberId);
        schedule.change(categoryForUpdate, request.getTitle(), request.getStartDateTime(), request.getEndDateTime(),
                request.getMemo());
    }

    @Transactional
    public void delete(final Long id, final Long memberId) {
        Schedule schedule = scheduleRepository.getById(id);
        schedule.validateEditPossible(memberId);
        scheduleRepository.deleteById(id);
    }

    public AvailablePeriodMaterial findInMembersByCategoryIdAndDateRange(final Long categoryId,
                                                                         final DateRangeRequest request) {
        List<Long> memberIds = toMemberIds(categoryId);
        List<Subscription> subscriptions = subscriptionRepository.findByMemberIdIn(memberIds);

        List<IntegrationSchedule> internalSchedules = toInternalSchedules(subscriptions, request);
        Map<String, List<ExternalRequestMaterial>> tokenByExternalIds = toTokenByExternalIds(subscriptions);

        return new AvailablePeriodMaterial(internalSchedules, tokenByExternalIds);
    }

    private List<Long> toMemberIds(final Long categoryId) {
        return subscriptionRepository.findByCategoryId(categoryId)
                .stream()
                .map(Subscription::getMember)
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    private List<IntegrationSchedule> toInternalSchedules(final List<Subscription> subscriptions,
                                                          final DateRangeRequest request) {
        List<Category> internalCategories = toInternalCategories(subscriptions);

        LocalDateTime startDate = request.getStartDateTime();
        LocalDateTime endDate = request.getEndDateTime();
        return scheduleRepository.getByCategoriesAndBetween(internalCategories, startDate, endDate);
    }

    private List<Category> toInternalCategories(final List<Subscription> subscriptions) {
        return subscriptions.stream()
                .filter(Subscription::hasInternalCategory)
                .map(Subscription::getCategory)
                .collect(Collectors.toList());
    }

    private Map<String, List<ExternalRequestMaterial>> toTokenByExternalIds(final List<Subscription> subscriptions) {
        return subscriptions.stream()
                .filter(Subscription::hasExternalCategory)
                .map(this::toExternalRequestMaterial)
                .collect(Collectors.groupingBy(ExternalRequestMaterial::getRefreshToken));
    }

    private ExternalRequestMaterial toExternalRequestMaterial(final Subscription subscription) {
        String refreshToken = toRefreshToken(subscription);
        Category category = subscription.getCategory();
        String externalId = toExternalId(category);
        return new ExternalRequestMaterial(refreshToken, category.getId(), externalId);
    }


    private String toRefreshToken(final Subscription subscription) {
        Member member = subscription.getMember();
        OAuthToken oAuthToken = oAuthTokenRepository.getByMemberId(member.getId());
        return oAuthToken.getRefreshToken();
    }

    private String toExternalId(final Category category) {
        ExternalCategoryDetail externalCategoryDetail = externalCategoryDetailRepository.getByCategory(category);
        return externalCategoryDetail.getExternalId();
    }
}

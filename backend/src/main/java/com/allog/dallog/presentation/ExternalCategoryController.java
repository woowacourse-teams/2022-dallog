package com.allog.dallog.presentation;

import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.category.dto.request.ExternalCategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.composition.application.CategorySubscriptionService;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarService;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarsResponse;
import com.allog.dallog.presentation.auth.AuthenticationPrincipal;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/external-calendars/me")
@RestController
public class ExternalCategoryController {

    private final ExternalCalendarService externalCalendarService;
    private final CategorySubscriptionService categorySubscriptionService;

    public ExternalCategoryController(final ExternalCalendarService externalCalendarService,
                                      final CategorySubscriptionService categorySubscriptionService) {
        this.externalCalendarService = externalCalendarService;
        this.categorySubscriptionService = categorySubscriptionService;
    }

    @GetMapping
    public ResponseEntity<ExternalCalendarsResponse> getExternalCalendar(
            @AuthenticationPrincipal final LoginMember loginMember) {

        return ResponseEntity.ok(externalCalendarService.findByMemberId(loginMember.getId()));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> save(@AuthenticationPrincipal final LoginMember loginMember,
                                                 @RequestBody final ExternalCategoryCreateRequest request) {
        CategoryResponse response = categorySubscriptionService.save(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/api/categories/" + response.getId())).body(response);
    }
}

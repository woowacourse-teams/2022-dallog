package com.allog.dallog.domain.externalcalendar.presentation;

import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.auth.presentation.AuthenticationPrincipal;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.request.ExternalCategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarService;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarsResponse;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/external-calendars/me")
@RestController
public class ExternalCalendarController {

    private final ExternalCalendarService externalCalendarService;
    private final CategoryService categoryService;

    public ExternalCalendarController(final ExternalCalendarService externalCalendarService,
                                      final CategoryService categoryService) {
        this.externalCalendarService = externalCalendarService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ExternalCalendarsResponse> getExternalCalendar(
            @AuthenticationPrincipal final LoginMember loginMember) {
        return ResponseEntity.ok(externalCalendarService.findByMemberId(loginMember.getId()));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> save(@AuthenticationPrincipal final LoginMember loginMember,
                                                 @Valid @RequestBody final ExternalCategoryCreateRequest request) {
        CategoryResponse response = categoryService.save(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/api/categories/" + response.getId())).body(response);
    }
}

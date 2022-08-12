package com.allog.dallog.presentation;

import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.presentation.auth.AuthenticationPrincipal;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/categories/{categoryId}/schedules")
    public ResponseEntity<ScheduleResponse> save(@AuthenticationPrincipal final LoginMember loginMember,
                                                 @PathVariable final Long categoryId,
                                                 @Valid @RequestBody final ScheduleCreateRequest request) {
        ScheduleResponse response = scheduleService.save(loginMember.getId(), categoryId, request);
        return ResponseEntity.created(URI.create("/api/schedules/" + response.getId())).body(response);
    }

    @GetMapping("/members/me/schedules")
    public ResponseEntity<MemberScheduleResponses> findSchedulesByMemberId(
            @AuthenticationPrincipal final LoginMember loginMember, @ModelAttribute DateRangeRequest request) {
        MemberScheduleResponses response = scheduleService.findSchedulesByMemberId(loginMember.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable final Long scheduleId) {
        ScheduleResponse response = scheduleService.findById(scheduleId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal final LoginMember loginMember,
                                       @PathVariable final Long scheduleId,
                                       @Valid @RequestBody final ScheduleUpdateRequest request) {
        scheduleService.update(scheduleId, loginMember.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal final LoginMember loginMember,
                                       @PathVariable final Long scheduleId) {
        scheduleService.deleteById(scheduleId, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}

package com.allog.dallog.domain.schedule.presentation;

import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.auth.presentation.AuthenticationPrincipal;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.dto.response.SchedulesResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/categories/{categoryId}/schedules")
    public ResponseEntity<Void> save(@AuthenticationPrincipal final LoginMember loginMember,
                                     @PathVariable final Long categoryId,
                                     @Valid @RequestBody final ScheduleCreateRequest request) {
        Long id = scheduleService.save(loginMember.getId(), categoryId, request);
        return ResponseEntity.created(URI.create("/api/schedules/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<SchedulesResponse> findByYearAndMonth(@RequestParam final int year,
                                                                @RequestParam final int month) {
        List<ScheduleResponse> responses = scheduleService.findByYearAndMonth(year, month);
        return ResponseEntity.ok(new SchedulesResponse(responses));
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable final Long scheduleId) {
        ScheduleResponse response = scheduleService.findById(scheduleId);
        return ResponseEntity.ok(response);
    }
}

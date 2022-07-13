package com.allog.dallog.schedule.controller;

import com.allog.dallog.global.dto.ListResponse;
import com.allog.dallog.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.schedule.service.ScheduleService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/schedules")
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody final ScheduleCreateRequest request) {
        Long id = scheduleService.save(request);
        return ResponseEntity.created(URI.create("/api/schedules/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<ListResponse<ScheduleResponse>> findByYearAndMonth(@RequestParam final int year,
                                                                             @RequestParam final int month) {
        List<ScheduleResponse> responses = scheduleService.findByYearAndMonth(year, month);
        return ResponseEntity.ok(new ListResponse<>(responses));
    }
}

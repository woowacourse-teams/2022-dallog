package com.allog.dallog.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorTestController {

    private final ErrorTestService errorTestService;

    public ErrorTestController(final ErrorTestService errorTestService) {
        this.errorTestService = errorTestService;
    }

    @GetMapping("/api/error")
    public ResponseEntity<Void> error() {
        errorTestService.invokeError();
        return ResponseEntity.internalServerError().build();
    }
}

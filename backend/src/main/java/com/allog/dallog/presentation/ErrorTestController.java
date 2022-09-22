package com.allog.dallog.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorTestController {

    @GetMapping("/api/error")
    public ResponseEntity<Void> error() throws Exception {
        throw new Exception("이거슨 에러 테스트다.");
    }
}

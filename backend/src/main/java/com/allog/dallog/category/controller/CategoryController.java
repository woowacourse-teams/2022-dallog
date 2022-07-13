package com.allog.dallog.category.controller;

import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.service.CategoryService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody final CategoryCreateRequest request) {
        Long id = categoryService.save(request);
        return ResponseEntity.created(URI.create("/api/categories/" + id)).build();
    }
}

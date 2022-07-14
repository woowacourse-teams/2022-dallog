package com.allog.dallog.category.controller;

import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.category.service.CategoryService;
import com.allog.dallog.global.dto.ListResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<ListResponse<CategoryResponse>> findSliceBy(@RequestParam final int page,
                                                                      @RequestParam final int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<CategoryResponse> responses = categoryService.findSliceBy(pageRequest);
        return ResponseEntity.ok(new ListResponse<>(responses));
    }
}

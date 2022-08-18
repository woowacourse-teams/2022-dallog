package com.allog.dallog.presentation;

import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.composition.application.CategorySubscriptionService;
import com.allog.dallog.presentation.auth.AuthenticationPrincipal;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final CategorySubscriptionService categorySubscriptionService;

    public CategoryController(final CategoryService categoryService,
                              final CategorySubscriptionService categorySubscriptionService) {
        this.categoryService = categoryService;
        this.categorySubscriptionService = categorySubscriptionService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> save(@AuthenticationPrincipal final LoginMember loginMember,
                                                 @Valid @RequestBody final CategoryCreateRequest request) {
        CategoryResponse categoryResponse = categorySubscriptionService.save(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/api/categories/" + categoryResponse.getId())).body(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<CategoriesResponse> findAllByName(@RequestParam(defaultValue = "") final String name,
                                                            final Pageable pageable) {
        return ResponseEntity.ok(categoryService.findNormalByName(name, pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<CategoriesResponse> findMineByName(@AuthenticationPrincipal final LoginMember loginMember,
                                                             @RequestParam(defaultValue = "") final String name,
                                                             final Pageable pageable) {
        return ResponseEntity.ok(categoryService.findMineByName(loginMember.getId(), name, pageable));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable final Long categoryId) {
        return ResponseEntity.ok().body(categoryService.findById(categoryId));
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal final LoginMember loginMember,
                                       @PathVariable final Long categoryId,
                                       @RequestBody final CategoryUpdateRequest request) {
        categoryService.update(loginMember.getId(), categoryId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal final LoginMember loginMember,
                                       @PathVariable final Long categoryId) {
        categoryService.deleteById(loginMember.getId(), categoryId);
        return ResponseEntity.noContent().build();
    }
}

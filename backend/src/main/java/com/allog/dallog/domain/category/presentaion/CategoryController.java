package com.allog.dallog.domain.category.presentaion;

import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.auth.presentation.AuthenticationPrincipal;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryDetailResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.categoryrole.application.CategoryRoleService;
import com.allog.dallog.domain.categoryrole.dto.request.CategoryRoleUpdateRequest;
import com.allog.dallog.domain.categoryrole.dto.response.SubscribersResponse;
import java.net.URI;
import javax.validation.Valid;
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
    private final CategoryRoleService categoryRoleService;

    public CategoryController(final CategoryService categoryService, final CategoryRoleService categoryRoleService) {
        this.categoryService = categoryService;
        this.categoryRoleService = categoryRoleService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> save(@AuthenticationPrincipal final LoginMember loginMember,
                                                 @Valid @RequestBody final CategoryCreateRequest request) {
        CategoryResponse categoryResponse = categoryService.save(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/api/categories/" + categoryResponse.getId())).body(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<CategoriesResponse> findNormalByName(@RequestParam(defaultValue = "") final String name) {
        return ResponseEntity.ok(categoryService.findNormalByName(name));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDetailResponse> findDetailCategoryById(@PathVariable final Long categoryId) {
        return ResponseEntity.ok(categoryService.findDetailCategoryById(categoryId));
    }

    @GetMapping("/me/schedule-editable") // 일정 추가, 수정 모달의 카테고리 목록에 사용됨
    public ResponseEntity<CategoriesResponse> findScheduleEditableCategories(
            @AuthenticationPrincipal final LoginMember loginMember) {
        return ResponseEntity.ok(categoryService.findScheduleEditableCategories(loginMember.getId()));
    }

    @GetMapping("/me/admin") // 카테고리 관리 페이지에 접근할 수 있는지 판단하기 위해 사용됨
    public ResponseEntity<CategoriesResponse> findAdminCategories(
            @AuthenticationPrincipal final LoginMember loginMember) {
        return ResponseEntity.ok(categoryService.findAdminCategories(loginMember.getId()));
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal final LoginMember loginMember,
                                       @PathVariable final Long categoryId,
                                       @Valid @RequestBody final CategoryUpdateRequest request) {
        categoryService.update(loginMember.getId(), categoryId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal final LoginMember loginMember,
                                       @PathVariable final Long categoryId) {
        categoryService.delete(loginMember.getId(), categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{categoryId}/subscribers")
    public ResponseEntity<SubscribersResponse> findSubscribers(@AuthenticationPrincipal final LoginMember loginMember,
                                                               @PathVariable final Long categoryId) {
        SubscribersResponse subscribers = categoryRoleService.findSubscribers(loginMember.getId(), categoryId);
        return ResponseEntity.ok(subscribers);
    }

    @PatchMapping("/{categoryId}/subscribers/{memberId}/role")
    public ResponseEntity<Void> updateRole(@AuthenticationPrincipal final LoginMember loginMember,
                                           @PathVariable final Long categoryId,
                                           @PathVariable final Long memberId,
                                           @RequestBody final CategoryRoleUpdateRequest request) {
        categoryRoleService.updateRole(loginMember.getId(), memberId, categoryId, request);
        return ResponseEntity.noContent().build();
    }
}

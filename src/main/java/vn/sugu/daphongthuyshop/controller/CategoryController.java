package vn.sugu.daphongthuyshop.controller;

import org.springframework.web.bind.annotation.RestController;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.dto.request.categoryRequest.CreateCategoryRequest;
import vn.sugu.daphongthuyshop.dto.request.categoryRequest.UpdateCategoryRequest;
import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.categoryResponse.CategoryResponse;
import vn.sugu.daphongthuyshop.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    APIResponse<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) throws Exception {
        return APIResponse.<CategoryResponse>builder()
                .data(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{currentName}")
    APIResponse<CategoryResponse> updateCategory(
            @PathVariable String currentName,
            @Valid @RequestBody UpdateCategoryRequest request) throws Exception {
        return APIResponse.<CategoryResponse>builder()
                .data(categoryService.updateCategory(currentName, request))
                .build();
    }

    @GetMapping
    APIResponse<Page<CategoryResponse>> searchCategory(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "name", direction = Direction.ASC) Pageable pageable) {
        return APIResponse.<Page<CategoryResponse>>builder()
                .data(categoryService.searchCategory(name, pageable))
                .build();
    }

    @DeleteMapping("/{name}")
    APIResponse<Void> deleteCategory(@PathVariable String name) throws Exception {
        categoryService.deleteCategory(name);
        return APIResponse.<Void>builder()
                .message("Danh mục đã được xóa")
                .build();
    }
}

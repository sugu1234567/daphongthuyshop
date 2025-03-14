package vn.sugu.daphongthuyshop.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.sugu.daphongthuyshop.dto.request.categoryRequest.CreateCategoryRequest;
import vn.sugu.daphongthuyshop.dto.request.categoryRequest.UpdateCategoryRequest;
import vn.sugu.daphongthuyshop.dto.response.categoryResponse.CategoryResponse;
import vn.sugu.daphongthuyshop.entity.Category;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.mapper.CategoryMapper;
import vn.sugu.daphongthuyshop.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CreateCategoryRequest request) {

        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }

        Category category = categoryMapper.toCategory(request);

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    public CategoryResponse updateCategory(String currentName, UpdateCategoryRequest request) {

        Category category = categoryRepository.findByName(currentName)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        if (!category.getProducts().isEmpty() && !category.getName().equals(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_NAME_CHANGE_NOT_ALLOWED);
        }

        if (!currentName.equals(request.getName()) && categoryRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        categoryMapper.updateCategory(category, request);

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    public void deleteCategory(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        if (!category.getProducts().isEmpty()) {
            category.setDeleted(true);
        } else {
            categoryRepository.delete(category);
        }

        if (category.isDeleted()) {
            categoryRepository.save(category);
        }
    }

    public Page<CategoryResponse> searchCategory(String name, Pageable pageable) {

        if (name == null || name.trim().isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        String normalizedName = name.trim().toLowerCase();

        Page<Category> categories = categoryRepository.findByNameContaining(normalizedName, pageable);
        return categories.map(categoryMapper::toCategoryResponse);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        return categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }
}

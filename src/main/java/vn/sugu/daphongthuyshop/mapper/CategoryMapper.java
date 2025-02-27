package vn.sugu.daphongthuyshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.sugu.daphongthuyshop.dto.request.categoryRequest.CreateCategoryRequest;
import vn.sugu.daphongthuyshop.dto.request.categoryRequest.UpdateCategoryRequest;
import vn.sugu.daphongthuyshop.dto.response.categoryResponse.CategoryResponse;
import vn.sugu.daphongthuyshop.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CreateCategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);

    void updateCategory(@MappingTarget Category category, UpdateCategoryRequest request);
}

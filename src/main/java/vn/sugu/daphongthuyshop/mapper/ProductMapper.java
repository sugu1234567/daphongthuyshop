package vn.sugu.daphongthuyshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.sugu.daphongthuyshop.dto.request.productRequest.CreateProductRequest;
import vn.sugu.daphongthuyshop.dto.request.productRequest.UpdateProductRequest;
import vn.sugu.daphongthuyshop.dto.response.productResponse.ProductResponse;
import vn.sugu.daphongthuyshop.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    Product toProduct(CreateProductRequest request);

    @Mapping(source = "category.categoryId", target = "categoryId")
    @Mapping(target = "imageUrls", expression = "java(product.getProductImages().stream().map(pi -> pi.getImageUrl()).collect(java.util.stream.Collectors.toList()))")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    void updateProduct(@MappingTarget Product product, UpdateProductRequest request);
}
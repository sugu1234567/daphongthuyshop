package vn.sugu.daphongthuyshop.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.dto.request.productRequest.CreateProductRequest;
import vn.sugu.daphongthuyshop.dto.request.productRequest.UpdateProductRequest;
import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.productResponse.ProductResponse;
import vn.sugu.daphongthuyshop.service.ProductService;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProductController {

    ProductService productService;

    @PostMapping(consumes = { "multipart/form-data" })
    APIResponse<ProductResponse> createProduct(
            @Valid @RequestPart("request") CreateProductRequest request,
            @RequestPart(value = "images", required = false) MultipartFile[] imageFiles) throws Exception {
        return APIResponse.<ProductResponse>builder()
                .data(productService.createProduct(request, imageFiles))
                .message("Thêm sản phẩm thành công")
                .build();
    }

    @PutMapping(value = "/{productId}", consumes = { "multipart/form-data" })
    APIResponse<ProductResponse> updateProduct(
            @PathVariable String productId,
            @Valid @RequestPart("request") UpdateProductRequest request,
            @RequestPart(value = "images", required = false) MultipartFile[] imageFiles) throws Exception {
        return APIResponse.<ProductResponse>builder()
                .data(productService.updateProduct(productId, request, imageFiles))
                .message("Cập nhật thành công")
                .build();
    }

    @DeleteMapping("/{productId}")
    APIResponse<Void> deleteProduct(@PathVariable String productId) throws Exception {
        productService.deleteProduct(productId);
        return APIResponse.<Void>builder()
                .message("Sản phẩm đã được xóa")
                .build();
    }

    @GetMapping
    APIResponse<Page<ProductResponse>> searchProduct(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) BigDecimal minPrice,
            @PageableDefault(size = 10, sort = "name", direction = Direction.ASC) Pageable pageable) {
        Page<ProductResponse> result = productService.searchProduct(name, categoryName, minPrice, pageable);
        return APIResponse.<Page<ProductResponse>>builder()
                .data(result)
                .message(result.isEmpty() ? "Không tìm thấy sản phẩm" : null)
                .build();
    }
}
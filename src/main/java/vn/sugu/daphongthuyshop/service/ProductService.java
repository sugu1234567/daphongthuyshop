package vn.sugu.daphongthuyshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.sugu.daphongthuyshop.dto.request.productRequest.CreateProductRequest;
import vn.sugu.daphongthuyshop.dto.request.productRequest.UpdateProductRequest;
import vn.sugu.daphongthuyshop.dto.response.productResponse.ProductResponse;
import vn.sugu.daphongthuyshop.entity.Category;
import vn.sugu.daphongthuyshop.entity.Product;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.mapper.ProductMapper;
import vn.sugu.daphongthuyshop.repository.CategoryRepository;
import vn.sugu.daphongthuyshop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    CloudinaryService cloudinaryService;

    public ProductResponse createProduct(CreateProductRequest request, MultipartFile[] imageFiles) throws Exception {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        Product product = productMapper.toProduct(request);
        product.setCategory(category);

        // Kiểm tra và khởi tạo productImages nếu null
        if (product.getProductImages() == null) {
            product.setProductImages(new ArrayList<>());
        }

        if (imageFiles != null && imageFiles.length > 0) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String imageUrl = cloudinaryService.uploadFile(file);
                    product.addImage(imageUrl);
                }
            }
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    public ProductResponse updateProduct(String productId, UpdateProductRequest request, MultipartFile[] imageFiles)
            throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if (request.getCategoryId() != null && !request.getCategoryId().equals(product.getCategory().getCategoryId())
                && !product.getOrderDetails().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_NAME_CHANGE_NOT_ALLOWED);
        }

        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            product.setCategory(category);
        }

        productMapper.updateProduct(product, request);

        // Kiểm tra và khởi tạo productImages nếu null
        if (product.getProductImages() == null) {
            product.setProductImages(new ArrayList<>());
        }

        if (imageFiles != null && imageFiles.length > 0) {
            product.getProductImages().clear();
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String imageUrl = cloudinaryService.uploadFile(file);
                    product.addImage(imageUrl);
                }
            }
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);
    }

    public void deleteProduct(String productId) {
        // Tìm sản phẩm
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Kiểm tra quy tắc nghiệp vụ: Nếu có đơn hàng thì chỉ ẩn
        if (!product.getOrderDetails().isEmpty()) {
            product.setDeleted(true);
        } else {
            productRepository.delete(product);
        }

        // Lưu thay đổi nếu ẩn
        if (product.isDeleted()) {
            productRepository.save(product);
        }
    }

    public Page<ProductResponse> searchProduct(String name, String categoryName, BigDecimal minPrice,
            Pageable pageable) {
        // Nếu không có tiêu chí nào, trả về trang rỗng
        if ((name == null || name.trim().isEmpty()) &&
                (categoryName == null || categoryName.trim().isEmpty()) &&
                minPrice == null) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // Chuẩn hóa tiêu chí
        String normalizedName = name != null ? name.trim().toLowerCase() : null;
        String normalizedCategoryName = categoryName != null ? categoryName.trim().toLowerCase() : null;

        // Tìm kiếm theo nhiều tiêu chí
        List<Product> products = productRepository.findByCriteria(normalizedName, normalizedCategoryName, minPrice,
                pageable);
        return new PageImpl<>(products.stream().map(productMapper::toProductResponse).toList(), pageable,
                products.size());
    }
}
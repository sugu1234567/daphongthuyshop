package vn.sugu.daphongthuyshop.controller;

import org.springframework.web.bind.annotation.*;
import vn.sugu.daphongthuyshop.dto.request.reviewRequest.ReviewRequest;
import vn.sugu.daphongthuyshop.dto.request.reviewRequest.UpdateReviewRequest;
import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.reviewResponse.ReviewResponse;
import vn.sugu.daphongthuyshop.service.ReviewService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {

    ReviewService reviewService;

    // Tạo đánh giá mới
    @PostMapping
    APIResponse<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse reviewResponse = reviewService.createReview(request);
        return APIResponse.<ReviewResponse>builder()
                .message("Đánh giá đã được tạo thành công")
                .data(reviewResponse)
                .build();
    }

    // Lấy danh sách đánh giá theo sản phẩm
    @GetMapping("/product/{productId}")
    APIResponse<List<ReviewResponse>> getReviewsByProduct(@PathVariable String productId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByProduct(productId);
        return APIResponse.<List<ReviewResponse>>builder()
                .message("Lấy danh sách đánh giá theo sản phẩm thành công")
                .data(reviews)
                .build();
    }

    // Cập nhật đánh giá
    @PutMapping("/{reviewId}")
    APIResponse<ReviewResponse> updateReview(
            @PathVariable String reviewId,
            @Valid @RequestBody UpdateReviewRequest request) {
        ReviewResponse reviewResponse = reviewService.updateReview(reviewId, request);
        return APIResponse.<ReviewResponse>builder()
                .message("Đánh giá đã được cập nhật thành công")
                .data(reviewResponse)
                .build();
    }

    // Xóa đánh giá
    @DeleteMapping("/{reviewId}")
    APIResponse<Void> deleteReview(@PathVariable String reviewId) {
        reviewService.deleteReview(reviewId);
        return APIResponse.<Void>builder()
                .message("Đánh giá đã được xóa thành công")
                .data(null)
                .build();
    }

    // Lấy tất cả đánh giá
    @GetMapping
    APIResponse<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews();
        return APIResponse.<List<ReviewResponse>>builder()
                .message("Lấy tất cả đánh giá thành công")
                .data(reviews)
                .build();
    }
}
package vn.sugu.daphongthuyshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import vn.sugu.daphongthuyshop.dto.request.reviewRequest.ReviewRequest;
import vn.sugu.daphongthuyshop.dto.request.reviewRequest.UpdateReviewRequest;
import vn.sugu.daphongthuyshop.dto.response.reviewResponse.ReviewResponse;
import vn.sugu.daphongthuyshop.entity.Order;
import vn.sugu.daphongthuyshop.entity.Product;
import vn.sugu.daphongthuyshop.entity.Review;
import vn.sugu.daphongthuyshop.entity.User;
import vn.sugu.daphongthuyshop.enums.OrderStatus;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.repository.OrderRepository;
import vn.sugu.daphongthuyshop.repository.ProductRepository;
import vn.sugu.daphongthuyshop.repository.ReviewRepository;
import vn.sugu.daphongthuyshop.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ReviewService {

    ReviewRepository reviewRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getUserId().toString())
                .fullName(review.getUser().getFullName())
                .productId(review.getProduct().getProductId())
                .productName(review.getProduct().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public ReviewResponse createReview(ReviewRequest request) {
        User user = getCurrentUser();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<Order> completedOrders = orderRepository.findByUserAndStatus(user, OrderStatus.COMPLETED);
        boolean hasPurchased = completedOrders.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .anyMatch(detail -> detail.getProduct().getProductId().equals(request.getProductId()));

        if (!hasPurchased) {
            throw new AppException(ErrorCode.REVIEW_NOT_ALLOWED);
        }

        boolean alreadyReviewed = reviewRepository.findByUserAndProduct(user, product).isPresent();
        if (alreadyReviewed) {
            throw new AppException(ErrorCode.ALREADY_REVIEWED);
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        return toReviewResponse(savedReview);
    }

    public List<ReviewResponse> getReviewsByProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<Review> reviews = reviewRepository.findByProduct(product);
        return reviews.stream()
                .map(this::toReviewResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse updateReview(String reviewId, UpdateReviewRequest request) {
        User user = getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);
        return toReviewResponse(updatedReview);
    }

    public void deleteReview(String reviewId) {
        User user = getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        reviewRepository.delete(review);
    }

    public List<ReviewResponse> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(this::toReviewResponse)
                .collect(Collectors.toList());
    }
}
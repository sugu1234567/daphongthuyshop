package vn.sugu.daphongthuyshop.dto.response.reviewResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    String reviewId;
    String userId;
    String fullName;
    String productId;
    int rating;
    String comment;
    LocalDateTime createdAt;
}
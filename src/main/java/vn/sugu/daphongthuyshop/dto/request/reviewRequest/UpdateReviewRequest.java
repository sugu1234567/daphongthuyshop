package vn.sugu.daphongthuyshop.dto.request.reviewRequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReviewRequest {
    @Min(value = 1, message = "RATING_MIN_1")
    @Max(value = 5, message = "RATING_MAX_5")
    int rating;

    @NotEmpty(message = "COMMENT_REQUIRED")
    String comment;
}
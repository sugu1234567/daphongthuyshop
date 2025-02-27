package vn.sugu.daphongthuyshop.dto.request.categoryRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UpdateCategoryRequest {
    @NotBlank(message = "IS_REQUIRED")
    String name;

    String description;
}

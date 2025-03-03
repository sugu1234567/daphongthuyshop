package vn.sugu.daphongthuyshop.dto.request.productRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {

    @NotBlank(message = "NAME_IS_REQUIRED")
    String name;

    String description;

    @Min(value = 0, message = "PRICE_MUST_BE_POSITIVE")
    BigDecimal price;

    @Min(value = 0, message = "STOCK_MUST_BE_NON_NEGATIVE")
    int stock;

    String categoryId;
}
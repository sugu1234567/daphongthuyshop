package vn.sugu.daphongthuyshop.dto.request.cartRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToCartRequest {

    @NotBlank(message = "PRODUCT_ID_IS_REQUIRED")
    String productId;

    @Min(value = 1, message = "QUANTITY_MUST_BE_POSITIVE")
    int quantity;
}
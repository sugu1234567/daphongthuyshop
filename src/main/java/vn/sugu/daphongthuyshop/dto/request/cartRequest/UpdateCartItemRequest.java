package vn.sugu.daphongthuyshop.dto.request.cartRequest;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCartItemRequest {

    @Min(value = 1, message = "QUANTITY_MUST_BE_POSITIVE")
    int quantity;
}
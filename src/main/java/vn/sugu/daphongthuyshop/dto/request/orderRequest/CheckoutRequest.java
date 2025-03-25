package vn.sugu.daphongthuyshop.dto.request.orderRequest;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CheckoutRequest {
    @NotEmpty(message = "Cart item IDs are required")
    List<String> cartItemIds;
    @NotEmpty(message = "SHIPPING_ADDRESS_IS_REQUIRED")
    String shippingAddress;
    @NotEmpty(message = "PAYMENT_METHOD_IS_REQUIRED")
    String paymentMethod;
    String note;
}

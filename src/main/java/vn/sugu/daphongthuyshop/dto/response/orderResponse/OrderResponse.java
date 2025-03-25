package vn.sugu.daphongthuyshop.dto.response.orderResponse;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private String orderId;
    private String userId;
    private String shippingAddress;
    private String note;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private String paymentMethod;

}
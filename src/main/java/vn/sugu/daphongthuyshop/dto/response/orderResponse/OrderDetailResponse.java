package vn.sugu.daphongthuyshop.dto.response.orderResponse;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponse {
    private String orderDetailId;
    private String orderId;
    private String productName;
    private String productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;
}
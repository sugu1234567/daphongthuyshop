package vn.sugu.daphongthuyshop.dto.response.orderResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopProductResponse {
    private String productId;
    private String productName;
    private String category;
    private int quantitySold;
    private BigDecimal revenue;
}

package vn.sugu.daphongthuyshop.dto.response.dashboardResponse;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopSellingProductResponse {
    String productId;
    String productName;
    String imageUrl;
    BigDecimal price;
    long totalSold; // Tổng số lượng đã bán
    BigDecimal totalRevenue; // Tổng doanh thu từ sản phẩm này
}
package vn.sugu.daphongthuyshop.dto.response.dashboardResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class RecentSoldProductResponse {
    String productId;
    String productName;
    String imageUrl;
    BigDecimal price;
    int quantitySold; // Số lượng trong đơn hàng gần nhất
    LocalDateTime soldAt; // Thời gian bán gần nhất
    String orderNumber; // Mã đơn hàng
}
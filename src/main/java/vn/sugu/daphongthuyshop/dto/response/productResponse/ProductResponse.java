package vn.sugu.daphongthuyshop.dto.response.productResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    String productId;
    String name;
    String description;
    BigDecimal price;
    int stock;
    List<String> imageUrls;
    String categoryId;
    boolean isDeleted;
}
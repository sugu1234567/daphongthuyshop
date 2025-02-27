package vn.sugu.daphongthuyshop.dto.response.categoryResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoryResponse {

    String categoryId;
    String name;
    String description;
}

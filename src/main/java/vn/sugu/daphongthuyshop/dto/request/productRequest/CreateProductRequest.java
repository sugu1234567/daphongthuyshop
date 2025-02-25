package vn.sugu.daphongthuyshop.dto.request.productRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateProductRequest {

    @NotBlank(message = "IS_REQUIRED")
    String name;

    @NotBlank(message = "IS_REQUIRED")
    String description;

    @NotBlank(message = "IS_REQUIRED")
    String price;

    @NotBlank(message = "IS_REQUIRED")
    int stock;

}

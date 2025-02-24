package vn.sugu.daphongthuyshop.dto.request.userRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.enums.Role;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UpdateUserRequest {

    @NotBlank(message = "IS_REQUIRED")
    String fullName;

    @NotBlank(message = "IS_REQUIRED")
    String dob;

    @NotBlank(message = "IS_REQUIRED")
    String gender;

    @NotBlank(message = "IS_REQUIRED")
    String phone;

    @NotBlank(message = "IS_REQUIRED")
    String street;

    @NotBlank(message = "IS_REQUIRED")
    String ward;

    @NotBlank(message = "IS_REQUIRED")
    String district;

    @NotBlank(message = "IS_REQUIRED")
    String province;

    Role role;

    public String getFullAddress() {
        return String.join(", ", street, ward, district, province);
    }
}

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

    @NotBlank(message = "NAME_IS_REQUIRED")
    String fullName;

    @NotBlank(message = "IS_REQUIRED")
    String dob;

    @NotBlank(message = "IS_REQUIRED")
    String gender;

    @NotBlank(message = "IS_REQUIRED")
    String phone;

    Role role;
}

package vn.sugu.daphongthuyshop.dto.request.userRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateUserRequest {

    @NotBlank(message = "IS_REQUIRED")
    String fullName;

    @NotBlank(message = "IS_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "IS_REQUIRED")
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    @NotBlank(message = "IS_REQUIRED")
    String dob;

    @NotBlank(message = "IS_REQUIRED")
    String gender;

    @NotBlank(message = "IS_REQUIRED")
    String phone;

    String avatarUrl;
}

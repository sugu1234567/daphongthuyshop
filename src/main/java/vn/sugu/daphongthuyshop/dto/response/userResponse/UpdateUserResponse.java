package vn.sugu.daphongthuyshop.dto.response.userResponse;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.enums.Role;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UpdateUserResponse {
    String fullName;
    String dob;
    String gender;
    String phone;
    String address;
    Role role;
}

package vn.sugu.daphongthuyshop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.enums.Role;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterResponse {
    String fullName;
    String email;
    String password;
    String dob;
    String phone;
    Role role;
}

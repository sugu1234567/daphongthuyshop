package vn.sugu.daphongthuyshop.dto.request.authRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.enums.Role;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterRequest {
    String fullName;
    String email;
    String password;
    String dob;
    String phone;
}

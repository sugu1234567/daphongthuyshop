package vn.sugu.daphongthuyshop.dto.response.userResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.enums.Role;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserResponse {

    String userId;
    String fullName;
    String email;
    String gender;
    String dob;
    String phone;
    String avatarUrl;
    Role role;

}

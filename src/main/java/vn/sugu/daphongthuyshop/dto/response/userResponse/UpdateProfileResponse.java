package vn.sugu.daphongthuyshop.dto.response.userResponse;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UpdateProfileResponse {
    String fullName;
    String dob;
    String gender;
    String phone;
    String avatarUrl;
}

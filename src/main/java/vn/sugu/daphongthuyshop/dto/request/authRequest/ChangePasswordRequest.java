package vn.sugu.daphongthuyshop.dto.request.authRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ChangePasswordRequest {
    String currentPassword;
    String newPassword;
}

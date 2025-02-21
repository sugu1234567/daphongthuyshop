package vn.sugu.daphongthuyshop.dto.request.authRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoginRequest {
    @NotBlank(message = "LOGIN_BLANK")
    String email;
    @NotBlank(message = "LOGIN_BLANK")
    String password;
}

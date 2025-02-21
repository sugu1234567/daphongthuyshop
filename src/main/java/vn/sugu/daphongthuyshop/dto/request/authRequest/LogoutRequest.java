package vn.sugu.daphongthuyshop.dto.request.authRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LogoutRequest {
    String accessToken;
}

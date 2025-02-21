package vn.sugu.daphongthuyshop.dto.response;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class IsAuthenticatedResponse {
    String fullName;
    String email;
    Role role;
}

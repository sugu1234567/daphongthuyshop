package vn.sugu.daphongthuyshop.dto.request.addressRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateShippingAddress {
    @NotBlank(message = "NAME_IS_REQUIRED")
    String fullName;

    @NotBlank(message = "PHONE_IS_REQUIRED")
    String phone;

    @JsonProperty("isDefault")
    boolean isDefault;

    @NotBlank(message = "IS_REQUIRED")
    String street;

    @NotBlank(message = "IS_REQUIRED")
    String ward;

    @NotBlank(message = "IS_REQUIRED")
    String district;

    @NotBlank(message = "IS_REQUIRED")
    String province;

    public String getFullAddress() {
        return String.join(", ", street, ward, district, province);
    }
}

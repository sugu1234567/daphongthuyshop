package vn.sugu.daphongthuyshop.dto.response.addressResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ShippingAddressResponse {
    private String userId;
    private String fullName;
    private String phoneNumber;
    private String address;
    private boolean isDefault;
}
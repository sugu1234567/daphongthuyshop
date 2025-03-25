package vn.sugu.daphongthuyshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import vn.sugu.daphongthuyshop.dto.request.addressRequest.CreateShippingAddress;
import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.addressResponse.ShippingAddressResponse;
import vn.sugu.daphongthuyshop.service.ShippingAddressService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipping-addresses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ShippingAddressController {

    ShippingAddressService shippingAddressService;

    @PostMapping
    APIResponse<ShippingAddressResponse> createShippingAddress(@Valid @RequestBody CreateShippingAddress request) {
        log.info("Received isDefault from request: {}", request.isDefault());
        return APIResponse.<ShippingAddressResponse>builder()
                .message("Địa chỉ giao hàng đã được tạo thành công")
                .data(shippingAddressService.createShippingAddress(request))
                .build();
    }

    @PutMapping("/{id}")
    APIResponse<ShippingAddressResponse> updateShippingAddress(@PathVariable String id,
            @Valid @RequestBody CreateShippingAddress request) {
        return APIResponse.<ShippingAddressResponse>builder()
                .message("Địa chỉ giao hàng đã được cập nhật thành công")
                .data(shippingAddressService.updateShippingAddress(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    APIResponse<Void> deleteShippingAddress(@PathVariable String id) {
        shippingAddressService.deleteShippingAddress(id);
        return APIResponse.<Void>builder()
                .message("Địa chỉ giao hàng đã được xóa thành công")
                .build();
    }

    @GetMapping
    APIResponse<List<ShippingAddressResponse>> getAllShippingAddresses() {
        return APIResponse.<List<ShippingAddressResponse>>builder()
                .message("Lấy danh sách địa chỉ giao hàng thành công")
                .data(shippingAddressService.getAllShippingAddresses())
                .build();
    }
}
package vn.sugu.daphongthuyshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.sugu.daphongthuyshop.dto.request.orderRequest.CheckoutRequest;
import vn.sugu.daphongthuyshop.dto.request.orderRequest.UpdateStatusRequest;
import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.orderResponse.OrderDetailResponse;
import vn.sugu.daphongthuyshop.dto.response.orderResponse.OrderResponse;
import vn.sugu.daphongthuyshop.service.OrderService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping("/checkout")
    APIResponse<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        OrderResponse orderResponse = orderService.checkout(request);
        return APIResponse.<OrderResponse>builder()
                .data(orderResponse)
                .build();
    }

    @GetMapping
    APIResponse<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return APIResponse.<List<OrderResponse>>builder()
                .message("Lấy danh sách đơn hàng thành công")
                .data(orders)
                .build();
    }

    @GetMapping("/{orderId}/details")
    APIResponse<List<OrderDetailResponse>> getOrderDetails(@PathVariable String orderId) {
        List<OrderDetailResponse> orderDetails = orderService.getOrderDetails(orderId);
        return APIResponse.<List<OrderDetailResponse>>builder()
                .message("Lấy chi tiết đơn hàng thành công")
                .data(orderDetails)
                .build();
    }

    @PutMapping("/{orderId}/cancel")
    APIResponse<Void> cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return APIResponse.<Void>builder()
                .message("Đơn hàng đã được hủy thành công")
                .data(null)
                .build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String orderId,
            @Valid @RequestBody UpdateStatusRequest request) {
        OrderResponse response = orderService.updateOrderStatus(orderId, request);
        return ResponseEntity.ok(response);
    }
}
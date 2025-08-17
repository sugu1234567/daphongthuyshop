package vn.sugu.daphongthuyshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import vn.sugu.daphongthuyshop.dto.request.vnpayRequest.CreatePaymentRequest;
import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.vnpayResponse.PaymentUrlResponse;
import vn.sugu.daphongthuyshop.entity.Order;
import vn.sugu.daphongthuyshop.enums.OrderStatus;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.repository.OrderRepository;
import vn.sugu.daphongthuyshop.service.OrderService;
import vn.sugu.daphongthuyshop.service.VNPayService;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayController {

    VNPayService vnPayService;
    OrderRepository orderRepository;
    OrderService orderService;

    @PostMapping("/create-payment")
    public ResponseEntity<APIResponse<PaymentUrlResponse>> createPayment(
            @RequestBody CreatePaymentRequest request,
            HttpServletRequest servletRequest) {

        // Tìm đơn hàng theo ID
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Tạo URL thanh toán
        String ipAddress = servletRequest.getRemoteAddr();
        String paymentUrl = vnPayService.createPaymentUrl(
                order.getOrderId(),
                order.getTotalPrice().longValue(),
                "Thanh toan don hang " + order.getOrderId(),
                ipAddress);

        PaymentUrlResponse response = new PaymentUrlResponse();
        response.setPaymentUrl(paymentUrl);
        return ResponseEntity.ok(
                APIResponse.<PaymentUrlResponse>builder()
                        .message("Tạo URL thanh toán thành công")
                        .data(response)
                        .build());
    }

    @PostMapping("/vnpay-return")
    public ResponseEntity<APIResponse<String>> vnpayReturn(@RequestBody Map<String, String> params) {
        // Kiểm tra tính hợp lệ của dữ liệu trả về
        if (vnPayService.validatePaymentResponse(params)) {
            String vnp_ResponseCode = params.get("vnp_ResponseCode");
            String vnp_TxnRef = params.get("vnp_TxnRef");

            // Lấy orderId từ vnp_TxnRef
            String orderId = vnPayService.extractOrderId(vnp_TxnRef);

            // Tìm đơn hàng theo ID
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            if ("00".equals(vnp_ResponseCode)) {
                // Thanh toán thành công
                if (order.getStatus() != OrderStatus.PROCESSING) {
                    orderService.processSuccessfulPayment(orderId, vnp_TxnRef);
                }
                return ResponseEntity.ok(
                        APIResponse.<String>builder()
                                .message("Thanh toán thành công")
                                .data("Thanh toán thành công")
                                .build());
            } else {
                // Thanh toán thất bại
                if (order.getStatus() != OrderStatus.CANCELED) {
                    orderService.processFaiiedPayment(orderId);
                }
                return ResponseEntity.ok(
                        APIResponse.<String>builder()
                                .message("Thanh toán thất bại")
                                .data("Thanh toán thất bại")
                                .build());
            }
        } else {
            return ResponseEntity.ok(
                    APIResponse.<String>builder()
                            .message("Dữ liệu không hợp lệ")
                            .data("Dữ liệu không hợp lệ")
                            .build());
        }
    }

    @GetMapping("/vnpay-ipn")
    public ResponseEntity<String> vnpayIpn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            params.put(paramName, paramValue);
        }

        if (vnPayService.validatePaymentResponse(params)) {
            String vnp_ResponseCode = params.get("vnp_ResponseCode");
            String vnp_TxnRef = params.get("vnp_TxnRef");

            String orderId = vnPayService.extractOrderId(vnp_TxnRef);

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            if ("00".equals(vnp_ResponseCode)) {
                if (order.getStatus() != OrderStatus.PROCESSING) {
                    orderService.processSuccessfulPayment(orderId, vnp_TxnRef);
                }
                return ResponseEntity.ok("OK");
            } else {
                if (order.getStatus() != OrderStatus.CANCELED) {
                    orderService.processFaiiedPayment(orderId);
                }
                return ResponseEntity.ok("NOT_OK");
            }
        } else {
            return ResponseEntity.ok("NOT_OK");
        }
    }
}
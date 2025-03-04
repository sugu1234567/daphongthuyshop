package vn.sugu.daphongthuyshop.controller;

import org.springframework.web.bind.annotation.*;
import vn.sugu.daphongthuyshop.dto.request.cartRequest.AddToCartRequest;
import vn.sugu.daphongthuyshop.dto.request.cartRequest.UpdateCartItemRequest;
import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.cartResponse.CartResponse;
import vn.sugu.daphongthuyshop.service.CartService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("api/v1/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    private final CartService cartService;

    @PostMapping
    APIResponse<CartResponse> addToCart(@Valid @RequestBody AddToCartRequest request) {
        CartResponse cartResponse = cartService.addToCart(request);
        return APIResponse.<CartResponse>builder()
                .message("Sản phẩm đã được thêm vào giỏ hàng")
                .data(cartResponse)
                .build();
    }

    @PutMapping("/{productId}")
    APIResponse<CartResponse> updateCartItem(@PathVariable String productId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        CartResponse cartResponse = cartService.updateCartItem(productId, request);
        return APIResponse.<CartResponse>builder()
                .message("Cập nhật thành công")
                .data(cartResponse)
                .build();
    }

    @DeleteMapping("/{productId}")
    APIResponse<CartResponse> removeFromCart(@PathVariable String productId) {
        CartResponse cartResponse = cartService.removeFromCart(productId);
        return APIResponse.<CartResponse>builder()
                .message("Sản phẩm đã được xóa khỏi giỏ hàng")
                .data(cartResponse)
                .build();
    }

    @GetMapping
    APIResponse<CartResponse> getCart() {
        CartResponse cartResponse = cartService.getCart();
        return APIResponse.<CartResponse>builder()
                .message("Lấy thông tin giỏ hàng thành công")
                .data(cartResponse)
                .build();
    }
}
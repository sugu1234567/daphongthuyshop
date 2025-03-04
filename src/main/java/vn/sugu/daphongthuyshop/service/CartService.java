package vn.sugu.daphongthuyshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.sugu.daphongthuyshop.dto.request.cartRequest.AddToCartRequest;
import vn.sugu.daphongthuyshop.dto.request.cartRequest.UpdateCartItemRequest;
import vn.sugu.daphongthuyshop.dto.response.cartResponse.CartResponse;
import vn.sugu.daphongthuyshop.entity.Cart;
import vn.sugu.daphongthuyshop.entity.CartItem;
import vn.sugu.daphongthuyshop.entity.Product;
import vn.sugu.daphongthuyshop.entity.User;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.repository.CartRepository;
import vn.sugu.daphongthuyshop.repository.ProductRepository;
import vn.sugu.daphongthuyshop.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String email = authentication.getName();
        log.info("Fetching user with email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public CartResponse addToCart(AddToCartRequest request) {
        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    log.info("Created new cart for user: {}", user.getEmail());
                    return cartRepository.save(newCart);
                });

        log.info("Cart items size before operation: {}", cart.getCartItems() != null ? cart.getCartItems().size() : 0);
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
            log.warn("Cart items was null, initialized new ArrayList");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        if (product.getStock() < request.getQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            item.setQuantity(newQuantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.getCartItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponse(savedCart);
    }

    public CartResponse updateCartItem(String productId, UpdateCartItemRequest request) {
        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        log.info("Cart items size before update: {}", cart.getCartItems() != null ? cart.getCartItems().size() : 0);
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
            log.warn("Cart items was null, initialized new ArrayList");
        }

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        Product product = cartItem.getProduct();
        if (product.getStock() < request.getQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        cartItem.setQuantity(request.getQuantity());
        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponse(savedCart);
    }

    public CartResponse removeFromCart(String productId) {
        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        log.info("Cart items size before remove: {}", cart.getCartItems() != null ? cart.getCartItems().size() : 0);
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
            log.warn("Cart items was null, initialized new ArrayList");
        }

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        cart.getCartItems().remove(cartItem);
        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponse(savedCart);
    }

    public CartResponse getCart() {
        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        log.info("Cart items size for get: {}", cart.getCartItems() != null ? cart.getCartItems().size() : 0);
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
            log.warn("Cart items was null, initialized new ArrayList");
        }

        return mapToCartResponse(cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartResponse.CartItemResponse> cartItemResponses = cart.getCartItems().stream()
                .map(item -> CartResponse.CartItemResponse.builder()
                        .cartItemId(item.getCartItemId())
                        .productId(item.getProduct().getProductId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .cartItems(cartItemResponses)
                .build();
    }
}
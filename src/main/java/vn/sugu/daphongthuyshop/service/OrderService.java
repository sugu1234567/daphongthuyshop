package vn.sugu.daphongthuyshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.sugu.daphongthuyshop.dto.request.orderRequest.CheckoutRequest;
import vn.sugu.daphongthuyshop.dto.request.orderRequest.UpdateStatusRequest;
import vn.sugu.daphongthuyshop.dto.response.orderResponse.OrderDetailResponse;
import vn.sugu.daphongthuyshop.dto.response.orderResponse.OrderResponse;
import vn.sugu.daphongthuyshop.entity.Cart;
import vn.sugu.daphongthuyshop.entity.CartItem;
import vn.sugu.daphongthuyshop.entity.Order;
import vn.sugu.daphongthuyshop.entity.OrderDetail;
import vn.sugu.daphongthuyshop.entity.Product;
import vn.sugu.daphongthuyshop.entity.User;
import vn.sugu.daphongthuyshop.enums.OrderStatus;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.repository.CartRepository;
import vn.sugu.daphongthuyshop.repository.OrderDetailRepository;
import vn.sugu.daphongthuyshop.repository.OrderRepository;
import vn.sugu.daphongthuyshop.repository.ProductRepository;
import vn.sugu.daphongthuyshop.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class OrderService {

    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    UserRepository userRepository;
    CartRepository cartRepository;
    ProductRepository productRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUser().getUserId().toString());
        response.setShippingAddress(order.getShippingAddress());
        response.setNote(order.getNote());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt());
        response.setPaymentMethod(order.getPaymentMethod());
        return response;
    }

    private OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
        OrderDetailResponse response = new OrderDetailResponse();
        response.setOrderDetailId(orderDetail.getOrderDetailId());
        response.setOrderId(orderDetail.getOrder().getOrderId());
        response.setProductName(orderDetail.getProduct().getName());
        response.setQuantity(orderDetail.getQuantity());
        response.setPrice(orderDetail.getPrice());
        response.setTotal(orderDetail.calculateTotal());
        return response;
    }

    public OrderResponse checkout(CheckoutRequest request) {
        // Lấy người dùng hiện tại
        User user = getCurrentUser();

        // Tìm giỏ hàng của người dùng
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Lọc các mục hàng được chọn
        List<CartItem> selectedItems = cart.getCartItems().stream()
                .filter(item -> request.getCartItemIds().contains(item.getCartItemId()))
                .collect(Collectors.toList());

        // Kiểm tra xem có mục hàng nào được chọn không
        if (selectedItems.isEmpty()) {
            throw new AppException(ErrorCode.NO_ITEMS_SELECTED);
        }

        // Kiểm tra tồn kho
        for (CartItem item : selectedItems) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
        }

        // Tính tổng giá
        BigDecimal totalPrice = selectedItems.stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tạo đơn hàng
        Order order = Order.builder()
                .user(user)
                .shippingAddress(request.getShippingAddress())
                .note(request.getNote())
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .paymentMethod(request.getPaymentMethod())
                .build();

        // Tạo chi tiết đơn hàng và set vào order
        List<OrderDetail> orderDetails = selectedItems.stream()
                .map(item -> OrderDetail.builder()
                        .order(order)
                        .product(item.getProduct())
                        .quantity(item.getQuantity())
                        .price(item.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setOrderDetails(orderDetails);

        // Lưu đơn hàng (sẽ tự động lưu cả chi tiết đơn hàng)
        Order savedOrder = orderRepository.save(order);

        // Cập nhật tồn kho
        for (CartItem item : selectedItems) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        // Xóa các mục đã thanh toán khỏi giỏ hàng
        cart.getCartItems().removeAll(selectedItems);
        cartRepository.save(cart);

        // Trả về response
        return toOrderResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        User user = getCurrentUser();
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderDetailResponse> getOrderDetails(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
        return orderDetails.stream()
                .map(this::toOrderDetailResponse)
                .collect(Collectors.toList());
    }

    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateOrderStatus(String orderId, UpdateStatusRequest request) {
        // Tìm đơn hàng theo ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Lấy người dùng hiện tại để kiểm tra quyền
        User currentUser = getCurrentUser();
        if (!order.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String newStatus = request.getNewStatus();

        // Chuyển đổi trạng thái mới thành enum và kiểm tra hợp lệ
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_STATUS); // Trạng thái không hợp lệ
        }

        // Kiểm tra logic nghiệp vụ (ví dụ: không cho phép cập nhật nếu đã hoàn thành
        // hoặc hủy)
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrorCode.ORDER_STATUS_UPDATE_NOT_ALLOWED);
        }

        // Cập nhật trạng thái
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        // Trả về response
        return toOrderResponse(updatedOrder);
    }
}